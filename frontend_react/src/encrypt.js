import CryptoJS from 'crypto-js'
function aes_encrypt (data) {
  // 定义加密密钥，此处使用的是 AES-256-CBC 加密算法，密钥长度为 32 位
  const key = CryptoJS.enc.Utf8.parse('0123456789abcdef0123456789abcdef')
  // 定义偏移量，此处使用的是 CBC 模式，偏移量长度为 16 位
  const iv = CryptoJS.enc.Utf8.parse('0123456789abcdef')
  // 使用 AES-256-CBC 加密算法对数据进行加密
  const ciphertext = encrypt_msg(JSON.stringify(data), key, iv)
  const transaction = ciphertext.toString()
  return transaction
}

function encrypt_msg (msg, key, iv) {
  return CryptoJS.AES.encrypt(msg, key, {
    iv,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7,
  })
}

export { aes_encrypt }