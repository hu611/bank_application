import Header from "../Utils/header"
import '../Transaction/style.scss'
function ApiIndex () {
  return (<div>
    <Header></Header>
    <ul className="main_list">
      <li>
        <a href="/apiRegistration">API注册</a>
        <br></br>
      </li>
    </ul>
  </div>)
}

export default ApiIndex