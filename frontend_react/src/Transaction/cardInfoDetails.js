import React, { useState, useEffect } from 'react'
import axios from 'axios'
import CryptoJS from 'crypto-js'
import { useNavigate, useLocation } from 'react-router-dom'
import '../axios_interceptor'
import { frontend_url, storage_url } from '../constants'
import { aes_decrypt } from '../encrypt'
import Header from '../Utils/header'
import './style.scss'

function CardInfoDetailsForm () {
  const [cardId, setCardId] = useState(0)
  const [cardDetails, setCardDetails] = useState({})
  const [cardOpenDate, setCardOpenDate] = useState("")

  const { search } = useLocation()
  const queryParams = new URLSearchParams(search)

  const requestCardDetails = async (cardId) => {
    try {
      const response = await axios.get(storage_url + "/account/getCardDetails?cardId=" + cardId)
      if (response.data.code === '-1') {
        alert(response.data.msg)
      } else {
        let res = response.data.result
        res = aes_decrypt(res)
        console.log(res)
        setCardDetails(res)
        const date = res.openingDate.year + "/" + res.openingDate.month + "/" + res.openingDate.dayOfMonth
        console.log(date)
        setCardOpenDate(date)
      }
    } catch (error) {
      console.log(error)
      alert("Something goes wrong Please log in again")
    }
  }

  useEffect(() => {
    setCardId(queryParams.get("cardId"))
    requestCardDetails(queryParams.get("cardId"))
  }, [])
  return (
    <div>
      <Header></Header>
      <div className='align_center'>
        <p>Card Number: {cardDetails.cardNo}</p>
        <p>PRC ID: {cardDetails.prcId}</p>
        <p>Opening Date: {cardOpenDate}</p>
        <p>Balance: {cardDetails.balance}</p>
        <p>Card Type: {cardDetails.cardType}</p>
      </div>
    </div>
  )

}

export default CardInfoDetailsForm