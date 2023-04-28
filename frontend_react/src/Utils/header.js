import { Link } from 'react-router-dom'
import './header.scss'
function Header () {
  return (
    <div id="home">
      <div className="wrapper">
        <script type="text/javascript" src="https://pic.bankofchina.com/bocappd/images/boc2013_jquery-min.js"></script>
        <script defer async src="">
        </script>
        <div className="top clearfix">
          <div className="top_links">
            <a>简体中文</a>
            <a>繁体中文</a>
            <a>English</a>
            <a target="_self">无障碍浏览</a>
          </div>
          <div className="top_menu">
            <p className="p_2013">本网站支持IPv6</p>
            <p className="p_2013" id="top_service">

              <Link to="/login">登录中行</Link>

            </p>

            <p className="p_2013" id="top_network">

              <Link to="/register">注册</Link>


              <a title="在线客服" className="top_zxkf">在线客服</a>


            </p>
          </div>
        </div>
        <div className="header">
          <div className="header_area">
            <h1 className="logo"><a title="中国银行" href='/'></a></h1>
            <div className="header_info">
              <p className="phone">服务热线：<span>110</span>信用卡热线：<span>110</span></p>
              <form name="trssearchform" id="trssearchform" action="https://srh.bankofchina.com/search/sitesearch/index.jsp"
                method="post" target="_blank">
                <input type="hidden" name="searchColumn" value="all" />
                <div className="search_bar">
                  <input type="text" className="search_ipt" id="sword" name="sword" value="请输入检索关键词"
                    onFocus="if(this.value=='请输入检索关键词') this.value='';"
                    onBlur="if(this.value == '') this.value='请输入检索关键词';" />
                  <input type="button" className="search_btn" />
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Header