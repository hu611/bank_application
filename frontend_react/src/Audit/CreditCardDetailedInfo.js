import { useState, useEffect } from "react"
import { useLocation } from 'react-router-dom'
import { aes_encrypt } from "../encrypt"
import axios from "axios"
import { audit_url, credit_url } from "../constants"
import './style.scss'

function CreditCardDetailedInfo () {
  const [prcId, setPrcId] = useState('')
  const [imageUrls, setImageUrls] = useState('')
  const { search } = useLocation()
  const [creditScore, setCreditScore] = useState('')
  const queryParams = new URLSearchParams(search)
  useEffect(() => {

    setPrcId(queryParams.get('prcId'))
    fetchCreditCardData(queryParams.get('prcId'))
  }, [])

  const agreeCreditCardRequest = async () => {
    const data = {
      prcId: prcId,
      creditScore: creditScore
    }
    const sent_data = aes_encrypt(data)
    const transaction = {
      transaction: sent_data
    }
    try {
      const response = await axios.post(credit_url + "/credit/registerCreditCard", transaction)
    } catch (error) {
      alert("gg")
    }
  }

  const fetchCreditCardData = async (prcId) => {
    let json = []
    try {
      console.log(prcId)
      const response = await axios.get(audit_url + "/audit/getImageUrls/CreditAudit/" + prcId)
      console.log(response)
      const res = response.data
      const prefix = audit_url + "/audit/image/" + prcId + "/"
      for (let i = 0; i < res.length; i++) {
        json.push(prefix + res[i])
      }
    } catch (error) {
      alert("gg")
    }

    setImageUrls(json)
    console.log("imageUrls are " + json)
  }



  return (
    <div>
      <tbody>
        {imageUrls && imageUrls.map((imageUrl, index) => (
          <img key={index} src={imageUrl} alt={`Image ${index}`} />
        ))}
      </tbody>
      <div>
        <input
          type="number"
          id="amount"
          value={creditScore}
          onChange={(e) => setCreditScore(Number(e.target.value))}
        />
      </div>
      <div>
        <button onClick={agreeCreditCardRequest}>同意</button>
        <button>拒绝</button>
      </div>
    </div>
  )
}
export default CreditCardDetailedInfo