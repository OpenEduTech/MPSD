import logo from '../../../../images/logo.png'
import styless from './home.module.css';
import http from '../../../../utils/request';
import { useEffect, useState, useRef, MutableRefObject } from 'react';
import { List, MenuList, SelectObj } from './hometype';
import Select from 'react-select';
import makeAnimated from 'react-select/animated';
const animatedComponents = makeAnimated();
function Sentence() {
  const obj = {
    content: '',
    author: '',
    origin: '',
    category: ''
  }
  let timer: NodeJS.Timeout
  let checkRef: MutableRefObject<HTMLDivElement | null> = useRef(null)
  const [flag, setFlag] = useState<Array<string>>()
  const [selectList, setSelectList] = useState<Array<SelectObj>>()
  const [list, setList] = useState(obj)
  useEffect(() => {
    getList()
    getMenu()
  }, [])
  function getList(url: string = '/api/v1/all') {
    http.get<List>(url).then(res => {
      setList(res)
      setFlag(res.category.split('-'))
    })
  }
  function getMenu() {
    http.get<MenuList>('/api/v1').then(res => {
      let selectRes: Array<SelectObj> = []
      res.list.forEach(i => {
        for (const key in i) {
          selectRes.push({
            label: key,
            value: i[key]
          })
        }
      })
      setSelectList(selectRes)
    })
  }
  function checkOut() {
    if (timer) clearTimeout(timer)

    if (checkRef && checkRef.current) {
      checkRef.current.className = styless.checkout + ' ' + styless.debounce
    }

    timer = setTimeout(() => {
      getList()
      if (checkRef && checkRef.current) {
        checkRef.current.className = styless.checkout
      }
    }, 1000);
  }
  function onSelectChange(value: SelectObj) {
    getList('/api/v1' + value.value.replace('https://v1.jinrishici.com', ''))
  }

  return (
    <div className={styless.home}>
      <div className={styless.logo}>
        <img src={logo} alt='' />
        <div style={{ width: '200px', height: '50px', lineHeight: '30px' }}>
          <Select theme={(theme) => ({
            ...theme,
            borderRadius: 0,
            colors: {
              ...theme.colors,
              primary25: 'green',
              primary: 'green',
              primary50: "#00b300"
            },
          })} onChange={(value: any) => { onSelectChange(value) }} isSearchable={false} components={animatedComponents} isClearable={false} options={selectList} />
        </div>
      </div>
      <div className={styless.cont}>
        <div style={{ marginBottom: '20px' }}>《{list.origin}》</div>
        <h1>{list.content}</h1>
        <h2>
          {
            flag && flag.map((i, idx) => <div key={idx}>{i}</div>)
          }
        </h2>
        <div className={styless.auth}>--{list.author}</div>
        <div ref={checkRef} onClick={checkOut} className={styless.checkout}>换一句</div>
      </div>
    </div>
  )
}
export default Sentence