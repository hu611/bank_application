import axios from 'axios'
// 在请求被发送之前做一些事情
axios.interceptors.request.use(config => {
  // Do something before request is sent

  const jwt_token = localStorage.getItem('jwtToken')
  if (jwt_token) {
    // 将token设置到authorization请求头中
    config.headers.authorization = `Bearer ${jwt_token}`
  }

  return config
}, error => {
  // Do something with request error
  return Promise.reject(error)
})

// 在响应之前做一些事情
axios.interceptors.response.use(response => {
  // Do something with response data
  return response
}, error => {
  // Do something with response error
  return Promise.reject(error)
})
