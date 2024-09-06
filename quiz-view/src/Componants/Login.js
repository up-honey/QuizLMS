import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function Login({ setIsLoggedIn, setUsername}) {
    const [loginUsername, setLoginusername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();

        try{
            const response = await fetch('/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: loginUsername,
                    password,
                }),
                credentials: 'include', //세션 관리용 쿠키를 포함.
            });

            if (response.ok) {
                alert('Login 성공!');
                setIsLoggedIn(true);
                setUsername(loginUsername);
                navigate('/');
                console.log("로그인 성공" + loginUsername);
            } else{
                setErrorMessage('Login 실패!');
            }
        } catch(error){
            console.error('Login error:', error);
            setErrorMessage('네트워크 오류가 발생했습니다.');
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
                    <button type="submit">로그인</button>
                </form>
                {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
            </div>
        </div>
    );



}

export default Login;