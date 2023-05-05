import React, { useState } from 'react'

function Transfer () {
  const [fromAccount, setFromAccount] = useState('')
  const [toAccount, setToAccount] = useState('')
  const [amount, setAmount] = useState(0)
  const [status, setStatus] = useState('')

  const handleTransfer = () => {
    // 在这里写转账逻辑
    setStatus(`成功转账 ${amount} 元从账户 ${fromAccount} 到账户 ${toAccount}`)
    setFromAccount('')
    setToAccount('')
    setAmount(0)
  }

  return (
    <div>
      <h1>转账</h1>
      <form>
        <div>
          <label htmlFor="fromAccount">转出账户：</label>
          <input
            type="text"
            id="fromAccount"
            value={fromAccount}
            onChange={(e) => setFromAccount(e.target.value)}
          />
        </div>
        <div>
          <label htmlFor="toAccount">转入账户：</label>
          <input
            type="text"
            id="toAccount"
            value={toAccount}
            onChange={(e) => setToAccount(e.target.value)}
          />
        </div>
        <div>
          <label htmlFor="amount">转账金额：</label>
          <input
            type="number"
            id="amount"
            value={amount}
            onChange={(e) => setAmount(Number(e.target.value))}
          />
        </div>
        <button type="button" onClick={handleTransfer}>
          转账
        </button>
      </form>
      <div>{status}</div>
    </div>
  )
}

export default Transfer