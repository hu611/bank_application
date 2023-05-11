import React from 'react'
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom'
import LoginPage from './Login/login'
import Header from './Utils/header'
import Main from './main'
import Register from './Login/register'
import DepositForm from './Transaction/deposit'
import BankAccountForm from './Transaction/openaccount'
import Confirm_Open_Account from './Transaction/confirmaccount'
import Confirm_Credit_Account from './Transaction/opencreditaccount'
import BankCardsInfo from './Transaction/displaycardinfo'
import Transfer from './Transaction/transfer'
import CardInfoDetailsForm from './Transaction/cardInfoDetails'
import DebitPlanInfo from './Transaction/debitPlan'
function App () {
  return (
    <Router>
      <div>
        <Routes>
          <Route path='/' element={<Main />}></Route>
          <Route path="/login" element={<LoginPage />}> </Route>
          <Route path='/register' element={<Register />}></Route>
          <Route path='/deposit' element={<DepositForm />}></Route>
          <Route path='/cardInfo' element={<BankCardsInfo />}></Route>
          <Route path='/transfer' element={<Transfer />}></Route>
          <Route path='/cardInfoDetails' element={<CardInfoDetailsForm />}></Route>
          <Route path='/openAccount' element={<BankAccountForm />}></Route>
          <Route path='/confirmDebit' element={<Confirm_Open_Account />}></Route>
          <Route path='/confirmCredit' element={<Confirm_Credit_Account />}></Route>
          <Route path='/debitPlan' element={<DebitPlanInfo />}></Route>
        </Routes>
      </div>
    </Router>
  )
}

export default App;





