export interface List{
  content:string
  origin:string
  author:string
  category:string
}
interface MenuList{
  list:Array<Menu>
}
interface Menu {
  '全部'?: string;
  '抒情'?: string;
  '四季'?: string;
  '山水'?: string;
  '天气'?: string;
  '人物'?: string;
  '人生'?: string;
  '生活'?: string;
  '节日'?: string;
  '动物'?: string;
  '植物'?: string;
  '食物'?: string;
  '抒情-爱情'?: string;
  '抒情-友情'?: string;
  '抒情-离别'?: string;
  '抒情-思念'?: string;
  '抒情-思乡'?: string;
  '抒情-伤感'?: string;
  '抒情-孤独'?: string;
  '抒情-闺怨'?: string;
  '抒情-悼亡'?: string;
  '抒情-怀古'?: string;
  '抒情-爱国'?: string;
  '抒情-感恩'?: string;
  '四季-春天'?: string;
  '四季-夏天'?: string;
  '四季-秋天'?: string;
  '四季-冬天'?: string;
  '山水-庐山'?: string;
  '山水-泰山'?: string;
  '山水-江河'?: string;
  '山水-长江'?: string;
  '山水-黄河'?: string;
  '山水-西湖'?: string;
  '山水-瀑布'?: string;
  '天气-写风'?: string;
  '天气-写云'?: string;
  '天气-写雨'?: string;
  '天气-写雪'?: string;
  '天气-彩虹'?: string;
  '天气-太阳'?: string;
  '天气-月亮'?: string;
  '天气-星星'?: string;
  '人物-女子'?: string;
  '人物-父亲'?: string;
  '人物-母亲'?: string;
  '人物-老师'?: string;
  '人物-儿童'?: string;
  '人生-励志'?: string;
  '人生-哲理'?: string;
  '人生-青春'?: string;
  '人生-时光'?: string;
  '人生-梦想'?: string;
  '人生-读书'?: string;
  '人生-战争'?: string;
  '生活-乡村'?: string;
  '生活-田园'?: string;
  '生活-边塞'?: string;
  '生活-写桥'?: string;
  '节日-春节'?: string;
  '节日-元宵节'?: string;
  '节日-寒食节'?: string;
  '节日-清明节'?: string;
  '节日-端午节'?: string;
  '节日-七夕节'?: string;
  '节日-中秋节'?: string;
  '节日-重阳节'?: string;
  '动物-写鸟'?: string;
  '动物-写马'?: string;
  '动物-写猫'?: string;
  '植物-梅花'?: string;
  '植物-梨花'?: string;
  '植物-桃花'?: string;
  '植物-荷花'?: string;
  '植物-菊花'?: string;
  '植物-柳树'?: string;
  '植物-叶子'?: string;
  '植物-竹子'?: string;
  '食物-写酒'?: string;
  '食物-写茶'?: string;
  '食物-荔枝'?: string;
}
export interface SelectObj{
  label:string
  value:string
}