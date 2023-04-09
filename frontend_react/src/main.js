import Header from "./Utils/header"
import './main.scss'
function Main () {

  return (<div>
    <Header></Header>
    <ul className="main_list">
      <li>
        <a href="/transfer"> 转账</a>
      </li>
    </ul>
  </div>)
}

export default Main