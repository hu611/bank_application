import { useState, useEffect } from "react"
import { useLocation } from 'react-router-dom'
import '../axios_interceptor'
import { aes_encrypt } from "../encrypt"
import axios from "axios"
import { credit_url, storage_url } from "../constants"
import Header from "../Utils/header"

function Confirm_Credit_Account () {
  const [username, setUsername] = useState('')
  const [confirmcode, setConfirmcode] = useState('')
  const [pinNum, setPinNum] = useState('')
  const [IdCard, setIdCard] = useState('')
  const [salaryCard, setSalaryCard] = useState('')
  const [addressProof, setAddressProof] = useState('')
  const [taxDocument, setTaxDocument] = useState('')
  const { search } = useLocation()
  const queryParams = new URLSearchParams(search)
  useEffect(() => {
    setUsername(queryParams.get('username'))
    setConfirmcode(queryParams.get('confirmcode'))
  })

  async function handle_submit () {
    if (username.length === 0 || confirmcode.length === 0 || IdCard.length === 0
      || salaryCard.length === 0 || addressProof.length === 0 || taxDocument.length === 0) {
      //check if all information are input
      alert("Please input all information")
      return
    }
    if (pinNum.length != 4) {
      alert("Pin Number has to be 4 digits")
      return
    }

    var data = {
      username: username,
      confirmCode: confirmcode,
      pinNum: pinNum
    }

    const submit_data = aes_encrypt(data)
    data = {
      aes_message: submit_data,
      IdCard: IdCard,
      salaryCard: salaryCard,
      addressProof: addressProof,
      taxDocument: taxDocument
    }
    try {
      const response = await axios.post(storage_url + `/account/confirmCredit`, data, {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      })
      if (response.data.code === '-1') {
        alert(response.data.msg)
      } else {
        alert(response.data.msg)
      }
    } catch (error) {
      alert(JSON.stringify(error.response))
    }


  }

  return (
    <div>
      <Header></Header>
      <label htmlFor="pin_num">Pin Num</label>
      <br></br>
      <input type="text" name="pin_num" id="pin_num" value={pinNum} onChange={e => { setPinNum(e.target.value) }} />

      <br></br>
      <br></br>
      <label htmlFor="idCardImage">ID Card Image</label>
      <br></br>
      <input type="file" name="idCardImage" id="idCardImage" onChange={e => { setIdCard(e.target.files[0]) }} />

      <br></br>
      <br></br>
      <label htmlFor="salaryCardImage">Salary Card Image</label>
      <br></br>
      <input type="file" name="salaryCardImage" id="salaryCardImage" onChange={e => setSalaryCard(e.target.files[0])} />
      <br></br>
      <br></br>
      <label htmlFor="addressProofImage">Address Proof Image</label>
      <br></br>
      <input type="file" name="addressProofImage" id="addressProofImage" onChange={e => setAddressProof(e.target.files[0])} />
      <br></br>
      <br></br>
      <label htmlFor="taxDocumentImage">Tax Document Image</label>
      <br></br>
      <input type="file" name="taxDocumentImage" id="taxDocumentImage" onChange={e => setTaxDocument(e.target.files[0])} />
      <br></br>
      <br></br>
      <button type="submit" onClick={handle_submit}>Submit</button>
    </div>
  )
}

export default Confirm_Credit_Account