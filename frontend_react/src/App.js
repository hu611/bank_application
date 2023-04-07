import React from 'react'
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom'
import LoginPage from './Login/login'
import Header from './Utils/header'
import Register from './Login/register'
function App () {
  return (
    <Router>
      <div>



        <Routes>
          <Route path='/' element={<Header />}></Route>
          <Route path="/login" element={<LoginPage />}> </Route>
          <Route path='/register' element={<Register />}></Route>
        </Routes>
      </div>
    </Router>
  )
}

export default App;





