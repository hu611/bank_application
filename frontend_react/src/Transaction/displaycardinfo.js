import React, { useState, useEffect } from 'react'
import axios from 'axios'
import Header from '../Utils/header'
import './style.scss'
import '../axios_interceptor'
import { storage_url } from '../constants'

function BankCardsInfo () {
  const [cards, setCards] = useState([])

  const card_info_request = async () => {

    try {
      const response = await axios.get(storage_url + "/account/getCardInfo")
      if (response.data.code === '-1') {
        alert("Something goes wrong Please log in again")
      } else {
        const res = response.data.result
        for (let i = 0; i < res.length; i++) {
          const new_card = new Card(res[i].cardNum, res[i].cardType, res[i].id)
          setCards(prevCards => [...prevCards, new_card])


        }
        //console.log(cards)

      }
    } catch (error) {
      alert("Something goes wrong Please log in again")
    }

  }
  useEffect(() => {
    card_info_request()

    //console.log(cards)
  }, [])


  return (
    < div >
      <Header></Header>
      <h1 className='align_center'>All Bank Cards</h1>
      {
        cards.map((card) => (

          <div class="container" key={card.id}>
            <img class="card-image" src='https://www.visa.com.vc/dam/VCOM/regional/lac/ENG/Default/Pay%20With%20Visa/Find%20a%20Card/Debit%20Cards/Debit%20Classic/visaclassicdebit-400x225.jpg' />
            <div class="card-info">
              <p>银行卡号码：{card.cardNumber}</p>
              <p>银行卡类型：{card.cardType}</p>
              <div>
                <a href={'/deposit?cardId=' + card.id}>Deposit</a>
                <a href={'/login?redirect=/transfer&cardNum=' + card.cardNumber + "&cardId=" + card.id} style={{ marginLeft: '0.5em' }}>Transfer</a>
              </div>

            </div>
          </div>

        ))
      }

    </div >
  )
}

class Card {
  constructor(cardNumber, cardType, id) {
    this.cardNumber = cardNumber
    this.cardType = cardType
    this.id = id
  }
}

export default BankCardsInfo
