import axios from 'axios';
import { ElMessageBox } from 'element-plus';



const config = {
  // baseURL: process.env.baseURL
  baseURL: '/api',
  timeout: 1000,
  headers: {
    'Content-Type': 'application/json'
  },
};

const api = axios.create(config);

// 默认 post 请求，使用 application/json 形式
api.defaults.headers.post['Content-Type'] = 'application/json';


//封装下post
api.post = function(url, data){

      let token = window.localStorage.getItem('token');
      
      return new Promise((resolve, reject) => {
        axios({
          method: 'post',
          url:config.baseURL + url,
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

api.get = function(url){

  let token = window.localStorage.getItem('token');
  
  return new Promise((resolve, reject) => {
    axios({
      method: 'get',
      url:config.baseURL + url,
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

// http response 拦截器
api.interceptors.response.use(
  response => {
    //拦截响应，做统一处理
    if (response.data.code) {

      switch (response.status) {
        case 301:
      }
    }
    return response
  },
  //接口错误状态处理，也就是说无响应时的处理
  error => {
    return Promise.reject(error.response.status) // 返回接口返回的错误信息
  })

export default api;
