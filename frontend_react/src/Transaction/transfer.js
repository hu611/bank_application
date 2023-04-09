import React, { useState } from 'react'
import axios from 'axios'
import CryptoJS from 'crypto-js'

function TransferForm () {
  const [accountNumber, setAccountNumber] = useState('')
  const [amount, setAmount] = useState('')
  const [errorMessage, setErrorMessage] = useState('')

  function encrypt_msg (msg, key, iv) {
    return CryptoJS.AES.encrypt(msg, key, {
      iv,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7,
    })
  }
  const handleSubmit = async (event) => {
    event.preventDefault()
    const data = {
      account: accountNumber,
      amount: amount,
    }
    // 定义加密密钥，此处使用的是 AES-256-CBC 加密算法，密钥长度为 32 位
    const key = CryptoJS.enc.Utf8.parse('0123456789abcdef0123456789abcdef')
    // 定义偏移量，此处使用的是 CBC 模式，偏移量长度为 16 位
    const iv = CryptoJS.enc.Utf8.parse('0123456789abcdef')
    // 使用 AES-256-CBC 加密算法对数据进行加密
    const ciphertext = encrypt_msg(JSON.stringify(data), key, iv)
    console.log(key.toString())
    console.log(btoa(ciphertext.toString()))
    /*
    try {
      const response = await axios.post('/api/transfer', { accountNumber, amount })
      console.log(response.data) // do something with the response
    } catch (error) {
      setErrorMessage(error.message)
    }
    */
  }





  return (
    <form onSubmit={handleSubmit}>
      <label>
        账户号码:
        <input type="text" value={accountNumber} onChange={(e) => setAccountNumber(e.target.value)} />
      </label>
      <br />
      <label>
        数额:
        <input type="text" value={amount} onChange={(e) => setAmount(e.target.value)} />
      </label>
      <br />
      <button type="submit">转账</button>
      {errorMessage && <div>{errorMessage}</div>}
    </form>
  )
}

export default TransferForm
