import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:9090', // 스프링 부트 서버 주소
  withCredentials: true, // 쿠키를 포함한 요청 활성화
});

// CSRF 토큰을 요청 헤더에 자동으로 추가
// api.interceptors.request.use(config => {
//     const csrfToken = getCsrfToken();
//     if (csrfToken) {
//         config.headers['X-XSRF-TOKEN'] = csrfToken;
//     }
//     return config;
// });

// function getCsrfToken() {
//     return document.cookie.split('; ')
//     .find(row => row.startsWith('XSRF-TOKEN='))
//     ?.split('=')[1];
// }

export default api;