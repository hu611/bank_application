import React, { useState, useEffect } from 'react'
import axios from 'axios'
import CryptoJS from 'crypto-js'
import { useNavigate, useLocation } from 'react-router-dom'
import '../axios_interceptor'
import { frontend_url, storage_url } from '../constants'
import Header from '../Utils/header'
import './style.scss'

function cardInfoDetailsForm () {
  const { search } = useLocation()
  const navigate = useNavigate()
  const queryParams = new URLSearchParams(search)
  const [cardId, setCardId] = useState(0)
  const location = useLocation()

  async function requestCardDetails () {
    const response = axios.post(storage_url + "/")
  }

  useEffect(() => {
    setCardId(queryParams.get("cardId"))
  })
  return (
    <div>
      lol
    </div>
  )

}