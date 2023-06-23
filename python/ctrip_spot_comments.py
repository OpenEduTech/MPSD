from fake_useragent import UserAgent
import requests
import json

import os
import csv
import queue
from retrying import retry
import threading
import random
import datetime


def retry_if_Conn_error(exception):
    return isinstance(exception, ConnectionError)


def get_ip_list():
    root_dir = os.path.abspath('.')
    ip_config = os.path.join(root_dir, "ip.txt")
    ip_list = []
    with open(ip_config) as f:
        lines = f.readlines()
    for line in lines:

        li_dic = {'http': line.strip('\n')}
        ip_list.append(li_dic)
    return ip_list


@retry(retry_on_exception=retry_if_Conn_error)
def spider(cityName, spotName, spotId, pageno):
    url = "https://sec-m.ctrip.com/restapi/soa2/12530/json/viewCommentList?_fxpcqlniredt=09031150210510028466"

    data = {"pageid": 10650000804,
            "viewid": spotId,
            "tagid": 0,
            "pagenum": pageno,
            "pagesize": 100,
            "contentType": "json",
            "head": {
                "appid":
                "100013776",
                "cid": "09031150210510028466",
                "ctok": "",
                "lang": "01",
                "sid": "8888",
                "syscode": "09",
                "auth": "",
                "extension": []}
            }
    data = json.dumps(data).encode(encoding='utf-8')
    header = {
        'User-Agent': UserAgent().random,
    }
    proxies = random.choice(get_ip_list())
    resp = requests.post(url=url, data=data, proxies=proxies, headers=header)

    # print(resp.text)
    # return resp.text
    if resp.status_code == 200:
        comoment_list = get_spot_comments(cityName, spotName, resp)
        write_to_excel(comoment_list)
        print("写入{} {} 第 {} 页".format(cityName, spotName, pageno))
    else:
        print("请求失败")
        header['Connection'] = 'close'
        pass


def get_spot_comments(cityName, spotName, resp):
    data = json.loads(resp.text)
    comments_temp = data["data"]["comments"]
    comments_list = []
    for comments in comments_temp:
        temp = [cityName,
                spotName,
                comments['id'],
                comments['uid'],
                comments['memberLevel'],
                comments['memberName'],
                comments['date'],
                comments['score'],
                comments['sightStar'],
                comments['interestStar'],
                comments['costPerformanceStar'],
                comments['content']
                ]
        comments_list.append(temp)
    return comments_list


def write_to_excel(list):
    with open(csv_file, 'at', encoding='utf-8-sig', newline='') as fp:
        writer = csv.writer(fp)
        writer.writerows(list)


def init_csv():
    if os.path.exists(csv_file):
        os.remove(csv_file)
    headrow = [['城市', '景点名称', '用户id', '用户名称', '成员等级', '会员等级', '评论日期', '分数',
                '游玩指数', '兴趣指数', '消费指数', '评论内容']]
    write_to_excel(headrow)


def run_all_spots():
    '''多线程运行 目的是获取每一个实例的所有日志列表'''
    threadPools = []
    #
    task_size = 300
    for i in range(task_size):
        thread_name = 'Thread-spots-'+str(i)
        t = threading.Thread(
            target=get_all_comments, name=thread_name, kwargs={'comment_queue': comment_queue})
        threadPools.append(t)
    for t in threadPools:
        t.start()
    for t in threadPools:
        t.join()


def get_all_comments(comment_queue):
    while not comment_queue.empty():
        cityName, spotName, spotId, pageno = comment_queue.get()
        spider(cityName, spotName, spotId, pageno)


def enqueue_data(data, pageSum):
    for cityName in data:
        spotsInfo = data[cityName]
        for spot in spotsInfo:
            for pageNo in range(1, pageSum+1):
                comment_queue.put([cityName, spot[0], spot[1], pageNo])


if __name__ == '__main__':
    pageSum = 300
    start = datetime.datetime.now().replace(microsecond=0)
    print(start)
    root_dir = os.path.abspath('.')
    csv_file = os.path.join(root_dir, '携程景点评论.csv')
    init_csv()
    comment_queue = queue.Queue()
    data = {
        "北京": [["故宫", 229], ["北京野生动物园", 107469], ["八达岭长城", 230]],
        "苏州": [["拙政园", 47072], ["周庄", 109861], ["虎丘", 3763]],
        "广州": [["广州塔", 107540], ["长隆野生动物世界", 6802], ["广州岭南印象园", 110338]],
        "昆明": [["石林", 44950], ["滇池", 2967], ["七彩云南欢乐世界", 4682346]],
        "三亚": [["亚龙湾热带天堂森林公园", 74247], ["蜈支洲岛旅游风景区", 3244], ["天涯海角", 3222]],
        "成都": [['成都大熊猫繁育研究基地', 4229], ['都江堰景区', 4597], ['青城山', 62960], ['武侯祠', 4227], ['杜甫草堂', 4226], ['宽窄巷子', 63456], ['成都欢乐谷', 65834], ['漫花庄园', 1673197], ['锦里古街', 48862], ['成都海昌极地海洋公园', 85930], ['金沙遗址博物馆', 48863], ['成都博物馆', 2006697], ['蜀风雅韵川剧院', 138459], ['西岭雪山', 4349], ['成都动物园', 119895], ['芙蓉国粹变脸秀', 138441], ['春熙路', 138472], ['黄龙溪', 22176], ['都江堰中华大熊猫苑原熊猫乐园', 1683224], ['三星堆博物馆', 48854]],
        "西安":[['华山', 136698], ['秦始皇帝陵博物院兵马俑', 1444], ['西安城墙', 1449], ['长恨歌演出', 136558], ['华清宫', 1443], ['大唐芙蓉园', 26184], ['长安十二时辰主题街区', 133979300], ['陕西历史博物馆', 1446], ['大唐不夜城', 130441], ['大雁塔', 1442], ['西安钟楼', 52702], ['西安碑林博物馆', 140439], ['西安千古情', 5709479], ['大明宫国家遗址公园', 52685], ['西安鼓楼', 1410344], ['秦岭野生动物园', 73279], ['回民街', 54080], ['西安博物院', 52677], ['曲江海洋极地公园', 52697], ['终南山南五台景区', 6099]], 
        "杭州":[['灵隐寺', 2040], ['西湖', 49894], ['杭州宋城', 4081], ['OMG心跳乐园', 5716088], ['西溪国家湿地公园', 25481], ['西湖游船', 1410601], ['千岛湖中心湖区', 143589], ['千岛湖景区', 135831], ['雷峰塔', 2033], ['印象西湖最忆是杭州演出', 67402], ['灵隐飞来峰景区', 48020], ['清河坊街', 135781], ['大奇山国家森林公园', 49155], ['杭州野生动物世界', 25548], ['钱塘江夜游', 135838], ['千岛湖东南湖区', 135833], ['杭州长乔极地海洋公园', 110080], ['杭州动物园', 26649], ['千岛湖天屿景区', 5381359], ['杭州开元森泊度假乐园', 5012418]],
        "厦门":[['鼓浪屿', 57405], ['厦门园林植物园', 8625], ['钟鼓索道', 145125], ['厦门方特梦幻王国', 140141], ['曾厝垵', 109999], ['厦门大学', 14050], ['厦门科技馆', 50128], ['环岛路', 8669], ['南普陀寺', 2344], ['鹭江夜游', 139403], ['厦门方特旅游区', 2483147], ['厦门海上游', 133820986], ['潮汐之眼摩天轮', 135450276], ['日光岩', 2345], ['中山路步行街', 137032], ['灵玲马戏城', 1409728], ['胡里山炮台', 2342], ['菽庄花园', 2346], ['厦门海底世界', 8619], ['集美学村', 2343]],
        "洛阳":[['龙门石窟', 8865], ['老君山景区', 9348], ['白马寺', 8940], ['隋唐洛阳城应天门遗址', 5253398], ['隋唐洛阳城国家遗址公园天堂明堂景', 126715], ['洛阳博物馆', 77546], ['隋唐洛阳城九洲池', 5630385], ['中国国花园', 47077], ['洛邑古城', 2509137], ['龙潭大峡谷', 135601], ['少林寺', 7954], ['隋唐城遗址植物园', 1409396], ['重渡沟风景区', 57632], ['天子驾六博物馆', 47087], ['鸡冠洞', 9346], ['丽景门景区', 47076], ['白云山', 9324], ['关林庙', 141428], ['洛阳古墓博物馆', 9337], ['洛阳老街', 126540]],
        "重庆":[['武隆天生三桥', 45771], ['洪崖洞民俗风貌区', 50223], ['长江索道', 109940], ['重庆两江游', 1410372], ['磁器口古镇', 43811], ['武隆喀斯特旅游区', 112663], ['重庆欢乐谷', 2486251], ['解放碑步行街', 10386], ['重庆云端之眼观景台', 102734194], ['重庆动物园', 10377]]
      }
    enqueue_data(data, pageSum)
    run_all_spots()
    end = datetime.datetime.now().replace(microsecond=0)
    print(end)
    print('一共用时{}秒'.format(end-start))
