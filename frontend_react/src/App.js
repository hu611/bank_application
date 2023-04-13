import React from 'react'
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom'
import LoginPage from './Login/login'
import Header from './Utils/header'
import Main from './main'
import Register from './Login/register'
import TransferForm from './Transaction/transfer'
import BankAccountForm from './Transaction/openaccount'
import Confirm_Open_Account from './Transaction/confirmaccount'
function App () {
  return (
    <Router>
      <div>



        <Routes>
          <Route path='/' element={<Main />}></Route>
          <Route path="/login" element={<LoginPage />}> </Route>
          <Route path='/register' element={<Register />}></Route>
          <Route path='/transfer' element={<TransferForm />}></Route>
          <Route path='/openAccount' element={<BankAccountForm />}></Route>
          <Route path='/confirmOpenAccount' element={<Confirm_Open_Account />}></Route>
        </Routes>
      </div>
    </Router>
  )
}

export default App;





