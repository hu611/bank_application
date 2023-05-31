import { useState, useEffect } from "react"
import { useLocation } from 'react-router-dom'
import '../axios_interceptor'
import { aes_encrypt } from "../encrypt"
import axios from "axios"
import { storage_url } from "../constants"

function CreditCardDetailedInfo () {
  const [prcId, setPrcId] = useState('')
  const [pinNum, setPinNum] = useState('')
  const [imageUrls, setImageUrls] = useState('')
  const { search } = useLocation()
  const queryParams = new URLSearchParams(search)
  useEffect(() => {

    setPrcId(queryParams.get('prcId'))
    fetchCreditCardData()
  }, [])

  const fetchCreditCardData = async () => {
    let json = []
    json.push("https://pentagram-production.imgix.net/02b1692f-0938-4f33-8935-89aebccc7c61/LOL_Logo_Rendered_Hi-Res_onblue-4x3.jpg?crop=edges&fit=crop&h=630&rect=0%2C264%2C3754%2C2342&w=1200")
    setImageUrls(json)
    console.log(imageUrls)
  }



  return (
    <div>
      <tbody>
        {imageUrls && imageUrls.map((imageUrl, index) => (
          <img key={index} src={imageUrl} alt={`Image ${index}`} />
        ))}
      </tbody>
    </div>
  )
}
export default CreditCardDetailedInfo