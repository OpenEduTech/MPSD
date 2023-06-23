import { lazy, Suspense } from "react";
import { Navigate } from 'react-router-dom';
const Poem = lazy(() => import('../views/poem/home'))
// const Sentence = lazy(() => import('../views/sentence/home'))

const withLazyRouter = (el: JSX.Element): JSX.Element => {
  return <Suspense fallback='loading--'>
    {el}
  </Suspense>
}
interface Route {
  name?: string,
  path: string,
  children?: Array<Route>
  element: any
}
const route: Array<Route> = [
  {
    path: '/',
    element: <Navigate to='/poem' />
  },
  {
    name: 'poem',
    path: '/poem',
    element: withLazyRouter(<Poem />)
  },
  // {
  //   name: 'sentence',
  //   path: '/sentence',
  //   element: withLazyRouter(<Sentence />)
  // }
]
export default route