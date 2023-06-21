import axios from 'axios';
import { Http } from './request.d'
const token = localStorage.getItem('token') || ''

const requset = axios.create({
  timeout: 50000,
  baseURL: 'http://localhost:3000'
})

requset.interceptors.request.use((config) => {
  config.headers['X-User-Token'] = token
  return config
})

requset.interceptors.response.use((res) => {
  return res
}, (err) => {
  if (err.code !== 200) {
    Promise.reject(err)
  }
})


const http: Http = {
  get<T>(url: string, params?: object) {
    return new Promise<T>((resolve, reject) => {
      requset.get(url, params).then(res => {
        resolve(res?.data)
      })
        .catch(err => {
          reject(err)
        })
    })
  },
  post<T>(url: string, params?: object) {
    return new Promise<T>((resolve, reject) => {
      requset.get(url, params).then(res => {
        resolve(res?.data)
      })
        .catch(err => {
          reject(err)
        })
    })
  },
}

export default http