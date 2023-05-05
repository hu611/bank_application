import React, { useEffect, useState } from "react"
import Header from "../Utils/header"
import bcrypt from 'bcryptjs'
import { useLocation } from 'react-router-dom'
import { redirect, useNavigate } from "react-router-dom"

const get_salt_link = `http://localhost:63010/auth/getsalt`
async function getSalt (username) {
  let salt = ""
  //get salt value
  await fetch(get_salt_link + `?username=${username}`, {
    method: "GET",
  }).then((response) => response.text()).
    then((result) => salt = result)
  return salt
}

async function getToken (username, hash) {
  let login_response = ""
  const request_link = `http://localhost:63010/auth/oauth/token?client_id=XcWebApp&client_secret=XcWebApp&grant_type=password&username=${username}&password=${hash}`
  try {
    const response = await fetch(request_link, {
      method: "POST"
    }).then((response) => {
      if (!response.ok) {
        console.log(response.status)
        alert('wrong username or password')
      } else {
        return response.json()
      }
    }).then((data) => {
      login_response = data
      // 将 JWT 令牌存储在 localStorage 中

    })
    return login_response.access_token
  } catch {
    return "Unsuccessful"
  }

}

const SUCCESSFUL_LOGIN_MSG = "Login successful"
function LoginPage () {
  const navigate = useNavigate()
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const [redirectUrl, setRedirectUrl] = useState("")
  const [cardId, setCardId] = useState("")
  const { search } = useLocation()
  const queryParams = new URLSearchParams(search)
  useEffect(() => {
    setRedirectUrl(queryParams.get("redirect"))
    setCardId(queryParams.get("cardId"))
  })


  const handleUsernameChange = (event) => {
    setUsername(event.target.value)
  }

  const handlePasswordChange = (event) => {
    setPassword(event.target.value)
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    if (username.length === 0 || password.length === 0) {
      alert("Empty Username or password")
      return
    }
    // TODO: Add login logic here
    // get salt

    const salt = await getSalt(username)
    const hash = bcrypt.hashSync(password, salt)

    try {
      const jwtToken = await getToken(username, hash)
      console.log("successful")
      if (jwtToken !== null) {
        localStorage.setItem('jwtToken', jwtToken)
        if (redirectUrl !== null) {
          navigate(redirectUrl)
        } else {
          navigate('/')
        }
      }
    } catch (e) {
      console.log(e)
      alert("wrong password or username")
    }



  }

  return (
    <div>
      <Header></Header>
      <h1>Login</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Username:
          <input
            type="text"
            value={username}
            onChange={handleUsernameChange}
          />
        </label>
        <br />
        <label>
          Password:
          <input
            type="password"
            value={password}
            onChange={handlePasswordChange}
          />
        </label>
        <br />
        <button type="submit">Login</button>
      </form>
    </div>
  )
}

export default LoginPage
