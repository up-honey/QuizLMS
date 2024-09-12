import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../Css/formStyle.css"; // CSS 파일 임포트
import api from "./api";

function Login({ setIsLoggedIn, setUsername, setIsAdmin, checkLoginStatus }) {
    const [loginUsername, setLoginusername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [rememberMe, setRememberMe] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        
        try {
            const response = await api.post(
                '/login',
                `username=${encodeURIComponent(loginUsername)}&password=${encodeURIComponent(password)}&remember-me=${rememberMe}`,
                {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    withCredentials: true // 자격 증명 포함
                }
            );
            
            if (response.status === 200 && response.data.success) {
                alert('Login 성공!');
                await checkLoginStatus();
                navigate('/');
            } else {
                setErrorMessage(response.data.error || 'Login 실패!');
            }
        } catch (error) {
            console.error('Login error:', error);
            setErrorMessage(error.response?.data?.error || '네트워크 오류가 발생했습니다.');
        }
    };

    return (
        <div className="form-container">
            <div className="form-box">
                <h2>로그인</h2>
                <form className="login-form" onSubmit={handleLogin}>
                    <div>
                        <label>아이디</label>
                        <input
                            type="text"
                            value={loginUsername}
                            onChange={(e) => setLoginusername(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label>비밀번호</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
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
                    <button type="submit">로그인</button>
                    <Link to="/join" className="join">회원가입</Link>
                    {errorMessage && <p className="error-message">{errorMessage}</p>}
                </form>
            </div>
        </div>
    );
}

export default Login;
