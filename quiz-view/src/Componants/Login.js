import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "./api";

function Login({ setIsLoggedIn, setUsername}) {
    const [loginUsername, setLoginusername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [rememberMe, setRememberMe] = useState(false);
    const navigate = useNavigate();

    function getCsrfToken() {
        return document.cookie.split('; ')
        .find(row => row.startsWith('XSRF-TOKEN='))
        ?.split('=')[1];
    }

    const handleLogin = async (e) => {
        e.preventDefault();

        try{
            const csrfToken = getCsrfToken();
            const response = await api.post(
                '/login',
                `username=${encodeURIComponent(loginUsername)}&password=${encodeURIComponent(password)}&remember-me=${rememberMe}`,
                {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        'X-XSRF-TOKEN': csrfToken
                    },
                    withCredentials: true // 자격 증명 포함
                }
            );
            

            console.log(response);
            if (response.status === 200 && response.data.success) {
                alert('Login 성공!');
                setIsLoggedIn(true);
                setUsername(loginUsername);
                navigate('/');
                console.log("로그인 성공" + loginUsername);
            } else{
                setErrorMessage(response.data.error || 'Login 실패!');
            }
        } catch(error){
            console.error('Login error:', error);
            setErrorMessage(error.response?.data?.error || '네트워크 오류가 발생했습니다.');
        }
    };

    return (
        <div className="container">
            <div className="wrapper">
                <h2>로그인</h2>
                <form onSubmit={handleLogin}>
                    <div>
                        <label>로그인:</label>
                        <input
                            type="text"
                            value={loginUsername}
                            onChange={(e) => setLoginusername(e.target.value)}
                        />
                    </div>
                    <div>
                        <label>비밀번호:</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    <div>
                        <label>
                            <input
                                type="checkbox"
                                checked={rememberMe}
                                onChange={(e) => setRememberMe(e.target.checked)}
                            />
                            로그인 상태 유지
                        </label>
                    </div>
                    <div>
                        <button type="submit">로그인</button>
                        <Link to="/join">회원가입</Link>
                    </div>
                </form>
                {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
            </div>
        </div>
    );



}

export default Login;