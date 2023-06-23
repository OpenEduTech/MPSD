import styless from './index.module.css';
import type { MenuProps } from 'antd';
import { Menu } from 'antd';
function Home(props: any) {
    const items: MenuProps['items'] = [
        {
            label: '今日诗词',
            key: 'SENTENCE',
        },
        {
            label: '唐诗',
            key: 'TANGSHI',
        },
        {
            label: '宋诗',
            key: 'SONGSHI',
        },
        {
            label: '宋词',
            key: 'SONGCI',
        },
        {
            label: '元曲',
            key: 'YUANQU',
        },
    ];

    const { current, setCurrent } = props;

    const onClick: MenuProps['onClick'] = (e) => {
        setCurrent(e.key);
    };

    return (
        <div className={styless.menu}>
            <div className={styless.logo}>唐风宋韵</div>
            <Menu className="rightMenu" onClick={onClick} selectedKeys={[current]} mode="horizontal" items={items} />
        </div>
    )
}
export default Home