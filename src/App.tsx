import route from "./router";
import { useRoutes } from 'react-router-dom';
import http from "./utils/request";
import { useEffect } from "react";
interface Token {
  status: string
  data: string
}
function App() {
  const outlet = useRoutes(route)
  // useEffect(() => {
  //   localStorage.getItem('token') || http.get<Token>('/token').then(res => {
  //     localStorage.setItem('token', res?.data)
  //   })
  // }, [])
  return (
    <div className="App">
      {outlet}
    </div>
  );
}

// export default App;
// import React from "react";
// // import "./App.css";
// import { HashRouter, Route, Routes } from "react-router-dom";
// import Poem from "./views/poem/home";

// function App() {
//   return (
//     <div className="App">
//       <HashRouter>
//         <Routes>
//           <Route path='/' Component={Poem} />
//         </Routes>
//       </HashRouter>
//     </div>

//   );
// }

export default App;