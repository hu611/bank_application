import { useState, useEffect } from "react"
import { useLocation } from 'react-router-dom'
import '../axios_interceptor'
import { aes_encrypt } from "../encrypt"
import axios from "axios"
import { storage_url } from "../constants"
import Header from "../Utils/header"
import './style.scss'

function Confirm_Open_Account () {
  const [username, setUsername] = useState('')
  const [confirmcode, setConfirmcode] = useState('')
  const [pinNum, setPinNum] = useState('')
  const { search } = useLocation()
  const queryParams = new URLSearchParams(search)
  useEffect(() => {

    setUsername(queryParams.get('username'))
    setConfirmcode(queryParams.get('confirmcode'))


  })

  async function submit_confirm_account () {
    if (pinNum.length !== 4) {
      alert("Please enter 4 digit pinNum")
      return
    }
    const data = {
      username: username,
      confirmCode: confirmcode,
      pinNum: pinNum
    }
    const encrypted_msg = aes_encrypt(data)
    const confirm_msg = {
      "confirm_msg": encrypted_msg
    }
    try {
      const response = await axios.post(storage_url + `/account/confirmDebit`, confirm_msg)

      if (response.data.code === '-1') {
        alert(response.data.msg)
      } else {
        alert(response.data.msg)
      }
    } catch (error) {
      alert(error.msg)
    }
  }

  return (
    <div>
      <Header></Header>

      <label className="align_center">
        PIN:
        <input type="password" maxLength="4" value={pinNum} onChange={e => setPinNum(e.target.value)} />
      </label>
      <button type="submit" onClick={submit_confirm_account}>Submit</button>
    </div>
  )
}
export default Confirm_Open_Account
