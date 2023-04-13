import { useState, useEffect } from "react"
import { useLocation } from 'react-router-dom'
import '../axios_interceptor'
import axios from "axios"

function Confirm_Open_Account () {
  const [username, setUsername] = useState('')
  const [confirmcode, setConfirmcode] = useState('')
  const { search } = useLocation()
  const queryParams = new URLSearchParams(search)
  useEffect(() => {

    setUsername(queryParams.get('username'))
    setConfirmcode(queryParams.get('confirmcode'))
    console.log(username)
    console.log(confirmcode)
    /*
    try {
      const response = await axios.post(storage_url + `/account/confirm?username=${username}confirmcode=${confirmcode}`)
      if (response.data.code == '-1') {
        alert("Something goes wrong Please log in again")
      } else {
        alert(response.data.msg)
      }
    } catch (error) {
      alert("Something goes wrong Please log in again")
    }
    */
  })

  return (
    <div>
      "Hello"
    </div>
  )
}
export default Confirm_Open_Account
