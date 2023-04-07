import React, { useState } from "react"
import { useNavigate } from "react-router-dom"
import axios from 'axios'
import bcrypt from 'bcryptjs'
function Register () {
  const navigate = useNavigate()
  const [username, setUsername] = useState("")
  const [name, setName] = useState("")
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [cellphone, setCellphone] = useState('')
  const [prcId, setPrcId] = useState('')
  const [gender, setGender] = useState('')

  function handleSubmit (event) {
    event.preventDefault()
    if (password !== confirmPassword) {
      alert("confirm pwd is not the same as pwd")
      return
    }
    if (prcId.length === 0 | email.length === 0) {
      alert("has empty")
      return
    }
    const request_link = `http://localhost:63010/auth/register`
    const salt = bcrypt.genSaltSync(10) // 生成 salt
    const hash = bcrypt.hashSync(password, salt)
    console.log(hash)
    axios.post(request_link, {
      prcId: prcId,
      username: username,
      realname: name,
      gender: gender,
      salt: salt,
      cellPhone: cellphone,
      password: hash,
      email: email
    }).then((response) => {
      navigate('/login')
    }, (error) => {
      console.log(error)
    })
  }

  return (
    <div>
      <h1>注册页面</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="username">用户名：</label>
          <input type="text" id="username" value={username} onChange={(e) => setUsername(e.target.value)} />
        </div>
        <div>
          <label htmlFor="name">姓名：</label>
          <input type="text" id="name" value={name} onChange={(e) => setName(e.target.value)} />
        </div>
        <div>
          <label htmlFor="email">电子邮件地址：</label>
          <input type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)} />
        </div>
        <div>
          <label htmlFor="password">密码：</label>
          <input type="password" id="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </div>
        <div>
          <label htmlFor="confirmPassword">确认密码：</label>
          <input type="password" id="confirmPassword" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
        </div>
        <div>
          <label htmlFor="cellphone">手机号码：</label>
          <input type="text" id="cellphone" value={cellphone} onChange={(e) => setCellphone(e.target.value)} />
        </div>
        <div>
          <label htmlFor="prcId">身份证号码：</label>
          <input type="text" id="prcId" value={prcId} onChange={(e) => setPrcId(e.target.value)} />
        </div>
        <div>
          <label htmlFor="gender">性别：</label>
          <select id="gender" value={gender} onChange={(e) => setGender(e.target.value)}>
            <option value="">请选择性别</option>
            <option value="male">男</option>
            <option value="female">女</option>
          </select>
        </div>
        <button type="submit">注册</button>
      </form>
    </div>
  )
}

export default Register
