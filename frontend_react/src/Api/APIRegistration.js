import React, { useState } from 'react'
import { aes_decrypt, aes_encrypt } from '../encrypt'
import axios from "axios"
import { api_url } from '../constants'

function ApiRegistrationForm () {
  const [companyCredentialImage, setCompanyCredentialImage] = useState(null)
  const [debitCardInfo, setDebitCardInfo] = useState('')
  const [companyName, setCompanyName] = useState('')
  const [companyDescription, setCompanyDescription] = useState('')

  const handleSubmit = async (e) => {
    e.preventDefault()
    // 在这里进行提交逻辑
    // 可以使用收集到的数据进行后续处理或发送到服务器
    let aes_encrypt_data = {
      debitCardInfo: debitCardInfo,
      companyName: companyName,
      companyDescription: companyDescription
    }
    let companyInfo = aes_encrypt(aes_encrypt_data)
    let request = {
      companyCredentialFile: companyCredentialImage,
      companyInfo: companyInfo
    }
    const response = await axios.post(api_url + "/api/apiRegistration", request, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    })
    if (response.data.code === '-1') {
      //c
      alert(response.data.msg)
    } else {
      alert(response.data.msg)
    }

  }

  return (
    <div>
      <div>
        <label htmlFor="companyLogo">公司证明图片：</label>
        <input
          type="file"
          id="companyLogo"
          onChange={(e) => setCompanyCredentialImage(e.target.files[0])}
        />
      </div>

      <div>
        <label htmlFor="debitCardInfo">借记卡信息：</label>
        <input
          type="text"
          id="debitCardInfo"
          value={debitCardInfo}
          onChange={(e) => setDebitCardInfo(e.target.value)}
        />
      </div>

      <div>
        <label htmlFor="companyName">公司名称：</label>
        <input
          type="text"
          id="companyName"
          value={companyName}
          onChange={(e) => setCompanyName(e.target.value)}
        />
      </div>

      <div>
        <label htmlFor="companyDescription">公司介绍：</label>
        <textarea
          id="companyDescription"
          value={companyDescription}
          onChange={(e) => setCompanyDescription(e.target.value)}
        />

      </div>
      <button type="submit" onClick={handleSubmit}>注册</button>
    </div >


  )
}

export default ApiRegistrationForm