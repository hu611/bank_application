import React, { useState, useEffect } from 'react'
import axios from 'axios'
import Header from '../Utils/header'
import './style.scss'
import '../axios_interceptor'
import { storage_url } from '../constants'

function DebitPlanInfo () {
  const [debitPlans, setDebitPlans] = useState([])

  const card_info_request = async () => {

    try {
      const response = await axios.get(storage_url + "/plan/getAllPlans")
      if (response.data.code === '-1') {
        alert("Something goes wrong Please log in again")
      } else {
        const res = response.data.result
        for (let i = 0; i < res.length; i++) {
          const debitPlan = new DebitPlan(res[i].planTitle, res[i].planDesc, res[i].freezeAmount, res[i].interestRate, res[i].durationMonth, res[i].durationYear)
          setDebitPlans(prevPlans => [...prevPlans, debitPlan])

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
        debitPlans.map((debitplan) => (

          <div class="container" key={debitplan.id}>
            <img class="card-image" src='https://www.thesimpledollar.com/wp-content/uploads/2012/05/GettyImages-1264578858.jpg' />
            <div class="card-info">
              <p>计划标题：{debitplan.planTitle}</p>
              <p>计划金额: {debitplan.freezeAmount}</p>
              <p>计划描述: {debitplan.planDesc}</p>
              <p>计划周期：{debitplan.durationMonth}个月</p>
            </div>
          </div>


        ))
      }

    </div >
  )
}

class DebitPlan {
  constructor(planTitle, planDesc, freezeAmount, interestRate, durationMonth, durationYear) {
    this.planTitle = planTitle
    this.planDesc = planDesc
    this.freezeAmount = freezeAmount
    this.interestRate = interestRate
    this.durationMonth = durationMonth
    this.durationYear = durationYear
  }
}

export default DebitPlanInfo
