import { useState } from 'react'
import Header from '../Utils/header'
import '../axios_interceptor'
import axios from 'axios'
import { storage_url } from '../constants'

function BankAccountForm () {
  const [creditType, setCreditType] = useState('0') // 默认选项是借记卡
  const [isChecked, setIsChecked] = useState(false)

  const handleCreditTypeChange = (event) => {
    setCreditType(event.target.value)
  }

  async function handleSubmit () {
    // 在这里处理表单提交
    try {
      const response = await axios.post(storage_url + "/account/open?cardType=" + creditType)
      if (response.data.code === '-1') {
        alert("Something goes wrong Please log in again")
      } else {
        alert(response.data.msg)
      }
    } catch (error) {
      alert("Something goes wrong Please log in again")
    }
  }

  function handleCheckboxChange () {

    setIsChecked(!isChecked)
  }

  return (
    <div>
      <Header></Header>

      <div>
        <label htmlFor="creditType">Credit Type:</label>
        <select id="creditType" name="creditType" value={creditType} onChange={handleCreditTypeChange}>
          <option value="0">Debit Card</option>
          <option value="1">Credit Card</option>
        </select>
      </div>
      <p>
        <label>
          <input type="checkbox" checked={isChecked} onChange={handleCheckboxChange} />
          我已经阅读并同意了开户协议
        </label>
      </p>
      <button disabled={!isChecked} onClick={handleSubmit}>
        确认同意开户
      </button>
    </div>
  )
}
export default BankAccountForm
