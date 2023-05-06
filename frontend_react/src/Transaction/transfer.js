import React, { useEffect, useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import axios from 'axios'
import { frontend_url, storage_url } from '../constants'
import { aes_encrypt } from '../encrypt'
import Header from '../Utils/header'

function Transfer () {
  const [fromAccount, setFromAccount] = useState('')
  const [toAccount, setToAccount] = useState('')
  const [amount, setAmount] = useState('')
  const [status, setStatus] = useState('')
  const { search } = useLocation()
  const navigate = useNavigate()
  const queryParams = new URLSearchParams(search)

  const bank_account_request = async (card_id) => {

    try {
      const response = await axios.get(storage_url + "/account/getBankAccountById?Id=" + card_id)
      if (response.data.code === '-1') {
        alert("Something goes wrong Please log in again")
      } else {
        const res = response.data.result
        setFromAccount(res)
        console.log(res)

      }
    } catch (error) {
      alert("Something goes wrong Please log in again")
    }
  }

  useEffect(() => {
    const referer = document.referrer
    if (referer === null || referer !== frontend_url + "/cardInfo") {
      navigate('/')
    }
    bank_account_request(queryParams.get("cardId"))
    console.log(queryParams.get("cardId"))
  }, [])

  async function transferAction () {
    if (fromAccount.length === 0 || toAccount.length === 0 || amount == null) {
      alert("Please input everything in the box")
      return
    }
    const data = {
      senderBankAccount: fromAccount,
      recipientBankAccount: toAccount,
      transferAmount: amount.toString()
    }
    const sent_data = {
      "transaction": aes_encrypt(data)
    }
    console.log(sent_data)
    try {
      const response = await axios.post(storage_url + "/account/transfer", sent_data)
      if (response.data.code === '-1') {
        alert("Something goes wrong Please try again later")
      } else {
        const res = response.data.result
        setFromAccount(res)
        alert("Success")
        navigate('/')
      }
    } catch (error) {
      alert("Something goes wrong Please try again later")
    }
  }

  return (
    <div>
      <Header></Header>
      <h1>转账</h1>
      <form>
        <div>
          <label htmlFor="fromAccount">转出账户：</label>
          <input
            type="text"
            id="fromAccount"
            readonly value={fromAccount}
          />
        </div>
        <div>
          <label htmlFor="toAccount">转入账户：</label>
          <input
            type="text"
            id="toAccount"
            value={toAccount}
            onChange={(e) => setToAccount(e.target.value)}
          />
        </div>
        <div>
          <label htmlFor="amount">转账金额：</label>
          <input
            type="number"
            id="amount"
            value={amount}
            onChange={(e) => setAmount(Number(e.target.value))}
          />
        </div>
        <button type="button" onClick={transferAction}>
          转账
        </button>
      </form>
      <div>{status}</div>
    </div>
  )
}

export default Transfer