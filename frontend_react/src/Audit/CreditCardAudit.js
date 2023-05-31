import React, { useState, useEffect } from 'react'
import { audit_url } from '../constants'
import axios from 'axios'

const CreditCardAudit = () => {
  const [creditCards, setCreditCards] = useState([])

  useEffect(() => {
    // 在组件加载时获取信用卡信息的逻辑
    fetchCreditCardData()
  }, [])

  const fetchCreditCardData = async () => {
    let json = []
    try {
      const response = await axios.get(audit_url + "/audit/getCreditAudit")
      const res = response.data
      for (var i = 0; i < res.length; i++) {
        json.push(res[i])
        console.log(res[i])
      }
    } catch (error) {
      alert("gg")
    }

    // 更新信用卡信息的状态
    setCreditCards(json)
  }

  return (
    <div>
      <h1>Credit Card Audit</h1>
      <table>
        <thead>
          <tr>
            <th>PRCID</th>
            <th>Application Time</th>
          </tr>
        </thead>
        <tbody>
          {creditCards.map((card, index) => (

            <tr key={index}>
              <a href={'/creditAudit?prcId=' + card.prcId}>
                <td>{card.prcId}</td></a>
              <td>{card.applicationTime}</td>

            </tr>

          ))}
        </tbody>
      </table>
    </div>
  )
}

export default CreditCardAudit