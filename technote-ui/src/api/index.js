import axios from 'axios';

const config = {
  // baseURL: process.env.baseURL
  baseURL: import.meta.env.VITE_BASE_URL,
  timeout: 1000,
  headers: {
    'Content-Type': 'application/json'
  },
};

axios.interceptors.response.use((response) => {
  return response
}, function (error) {
  switch (error.response.status) {
    case 401:
      localStorage.removeItem('token')
      window.location.href='/#/login'
      break;
    default:
      break;
  }
  return Promise.reject(error.response.status)
});
const api = axios.create(config);

// 默认 post 请求，使用 application/json 形式
api.defaults.headers.post['Content-Type'] = 'application/json';

//封装下post
api.post = function (url, data) {
  let token = window.localStorage.getItem('token');
  return new Promise((resolve, reject) => {
    axios({
      method: 'post',
      url: config.baseURL + url,
      data,
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
        'Authorization': token
      },
    }).then(response => {
      if (response.status == 200) {
        //根据实际情况进行更改
        resolve(response)
      } else {
        reject(response)
      }
    })
  })
}

api.get = function (url) {

  let token = window.localStorage.getItem('token');

  return new Promise((resolve, reject) => {
    axios({
      method: 'get',
      url: config.baseURL + url,
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
        'Authorization': token
      },
    }).then(response => {
      if (response.status == 200) {
        //根据实际情况进行更改
        resolve(response)
      } else {
        reject(response)
      }
    })
  })

}

export default api;
