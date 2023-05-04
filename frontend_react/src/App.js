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
          <Route path='/transfer'></Route>
          <Route path='/openAccount' element={<BankAccountForm />}></Route>
          <Route path='/confirmDebit' element={<Confirm_Open_Account />}></Route>
          <Route path='/confirmCredit' element={<Confirm_Credit_Account />}></Route>
        </Routes>
      </div>
    </Router>
  )
}

export default App;





