import Header from "./Utils/header"
import './main.scss'
function Main () {

  return (<div>
    <Header></Header>
    <ul className="main_list">
      <li>
        <a href="/transfer"> 充值</a>
        <br></br>
        <a href="/openAccount"> 开户</a>
        <br></br>
      </li>
    </ul>
  </div>)
}

export default Main