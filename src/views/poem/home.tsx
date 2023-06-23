import styless from './home.module.css';
import http from '../../utils/request';
import { resData, Poem, PoemList, AuthorInfo, SortData } from './hometype';
import { useEffect, useState } from 'react';
import { message, Pagination, Drawer, Input } from 'antd';
import { Column } from '@ant-design/charts';
import MenuList from './components/menu/index';
import Sentence from './components/sentence/home';
import TANGSHI from '../../images/TANGSHI.png';
import SONGCI from '../../images/SONGCI.png';
import SONGSHI from '../../images/SONGSHI.png';
import YUANQU from '../../images/YUANQU.png';
import LiBai from '../../images/LiBai.png';
import LiQingZhao from '../../images/LiQingZhao.png';
import SuShi from '../../images/SuShi.png';
import XinQiJi from '../../images/XinQiJi.png';
import LiuYong from '../../images/LiuYong.png';

const nameKeys = ['李白', '李清照', '苏轼', '辛弃疾', '柳永']
const imgMap = {
  TANGSHI: TANGSHI,
  SONGCI: SONGCI,
  SONGSHI: SONGSHI,
  YUANQU: YUANQU,
  '李白': LiBai,
  '李清照': LiQingZhao,
  '苏轼': SuShi,
  '辛弃疾': XinQiJi,
  '柳永': LiuYong
}
const { Search } = Input;
function Home() {
  const pageTitleMap = {
    TANGSHI: '唐诗',
    SONGSHI: '宋诗',
    SONGCI: '宋词',
    YUANQU: '元曲'
  };

  const [current, setCurrent] = useState('SENTENCE');
  const [poemList, setPoemList] = useState<PoemList>();
  const [authorInfo, setAuthorInfo] = useState<{ name: string, info: string }>();
  const [curPage, setCurPage] = useState(1);
  const [keyWord, setKeyWord] = useState('');

  const [open, setOpen] = useState(false);
  const [typeOpen, setTypeOpen] = useState(false);

  const [authorSort, setAuthorSort] = useState<any[]>([]);
  const [nameSort, setNameSort] = useState<any[]>([]);
  const [wordCloud, setWordCloud] = useState('');

  function getList() {
    http.get<resData>('/api/poem/poem/query/poemList', {
      params: {
        pageNum: curPage,
        pageSize: 10,
        poemEnum: current,
        keyWord,
      }
    }).then(res => {
      setPoemList(res?.data || {});
    })
  }

  const getWordCloud = () => {
    http.get<{ data: any }>('/api/poem/poem/query/wordCloud', { params: { poemEnum: current, name: authorInfo?.name } }).then(res => {
      setWordCloud(res?.data?.image);
      console.log('res?.data?.image: ', res?.data?.image);
    })
  }

  const onAuthorClick = (name: string) => {
    http.get<{ data: AuthorInfo }>('/api/poem/poem/query/authorInfo', { params: { name } }).then(res => {
      setAuthorInfo({
        name,
        info: res?.data?.description || '没有数据哦！'
      });
    })
    getWordCloud();
    setOpen(true);
  }

  useEffect(() => {
    getList();
  }, [current, curPage])

  const onStarClick = (id: number) => {
    http.get('/api/poem/favour', { params: { id, poemEnum: current } }).then(res => {
      message.success('点赞成功');
      getList();
    })
  }

  // 热门词牌排行
  const getHotName = () => {
    http.get<SortData>('/api/poem/poem/query/hotTitle').then(res => {
      const data = res.data;
      let keys = Object.keys(data);
      const _data = keys?.map((i) => {
        return {
          name: i,
          value: data[i],
        }
      })
      setNameSort(_data);
    })
    setTypeOpen(!typeOpen);
  }

  // 作品数量排行
  const onTypeClick = () => {
    http.get<SortData>('/api/poem/poem/query/quantity', { params: { poemEnum: current } }).then(res => {
      const data = res.data;
      let keys = Object.keys(data);
      const _data = keys?.map((i) => {
        return {
          name: i,
          value: data[i],
        }
      })
      setAuthorSort(_data);
    })
    if (current === 'SONGCI') {
      getHotName();
    }
    setTypeOpen(!typeOpen);
  }

  const onClose = () => {
    setOpen(false);
  }

  const color = ['#2D61F6', '#62CA76', '#685DFD', '#ECA450', '#FF6D6D', '#BE62BE'];

  const config1 = {
    data: authorSort,
    width: 500,
    height: 300,
    autoFit: true,
    color,
    xField: 'name',
    yField: 'value',
    seriesField: 'name',
  };

  const config2 = {
    data: nameSort,
    width: 500,
    height: 300,
    autoFit: true,
    color,
    xField: 'name',
    yField: 'value',
    seriesField: 'name',
  };

  return (
    <div className={styless.home}>
      <MenuList current={current} setCurrent={setCurrent} />
      {
        current === 'SENTENCE' ? <Sentence /> : (
          <>
            <h1 onClick={onTypeClick}>{pageTitleMap[current]}</h1>
            {
              poemList?.records?.length ? (
                <>
                  <div>
                    {
                      poemList?.records?.map((item: Poem) => {
                        return (
                          <div className={styless.poem} key={item.id}>
                            <div className={styless.title}> {item?.title}</div>
                            <div className={styless.author} onClick={() => { onAuthorClick(item.author) }}>{item?.author}</div>
                            <div className={styless.content}>
                              {
                                item?.content?.split('。').join('。|').split('|')?.map(i => {
                                  return <div className={styless.singleText}>{i}</div>
                                })
                              }
                            </div>
                            <div className={styless.star} onClick={() => { onStarClick(item.id) }}>点赞 ({item?.star}) </div>
                          </div>
                        )
                      })
                    }
                  </div>
                  <div className={styless.pagination}>
                    <Pagination showSizeChanger={false} defaultCurrent={1} current={curPage} onChange={(value) => { setCurPage(value) }} total={poemList?.total} />
                  </div>
                </>
              ) : (
                <div className={styless.empty}>啊哦，未查询到数据！</div>
              )
            }
            <div className={styless.searchBox}>
              <Search placeholder="请输入标题或作者" onChange={(e) => { setKeyWord(e.target.value) }} onSearch={getList} enterButton />
            </div>
          </>
        )
      }
      <Drawer
        key='author'
        title="作者信息"
        placement="right"
        onClose={onClose}
        width='700px'
        open={open}>
        <div>
          <div className={styless.title2}>
            {authorInfo?.name}
          </div>
          <div className={styless.authorInfo} >
            {authorInfo?.info}
          </div>
          <div className={styless.wordCloud}>
            <span className={styless.title2}>词云</span>
            <img src={nameKeys.includes(authorInfo?.name || '') ? imgMap[authorInfo?.name || ''] : wordCloud} />
          </div>
        </div>
      </Drawer>
      <Drawer
        key='type'
        title={pageTitleMap[current]}
        placement="right"
        onClose={() => { setTypeOpen(false) }}
        width='70%'
        open={typeOpen}>
        <div>
          <div className={styless.descTitle}>作者作品排行榜</div>
          <div className={styless.bottom}>
            <Column {...config1} />
          </div>
          {
            current === 'SONGCI' && (
              <div>
                <div className={styless.descTitle}>热门词牌排行耪</div>
                <div className={styless.bottom}>
                  <Column {...config2} />
                </div>
              </div>
            )
          }
          <div className={styless.descTitle}>词云</div>
          <div>
            <img src={imgMap[current]} />
          </div>
        </div>
      </Drawer>
    </div>
  )
}
export default Home