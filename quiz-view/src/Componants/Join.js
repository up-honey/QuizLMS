import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function Join() {
    const [formData, setFormData] = useState({
        name: "",
        username: "",
        password: "",
    });
    const navigate = useNavigate();
    const [error, setError] = useState(null);

    // Handle input change
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    function getCsrfToken() {
        return document.cookie.split('; ')
        .find(row => row.startsWith('XSRF-TOKEN='))
        ?.split('=')[1];
    }

    const handleJoin = async (e) => {
        e.preventDefault();

        try{
            const csrfToken = getCsrfToken();
            const response = await fetch('/api/members/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': csrfToken
                },
                body: JSON.stringify(formData),
            });

            console.log(response);
            if(response.ok) {
                console.log('회원가입 성공', formData);
                navigate("/login");
            } else {
                const errorData = await response.json();
                setError(errorData.message || "회원가입 실패");
            }
        } catch (error) {
            setError("회원가입 에러 발생");
            console.error("Error:", error);
        }
    };

    return (
        <div className="join-form">
            <h2>회원가입</h2>
            <form onSubmit={handleJoin}>
                <div>
                    <label>이름</label>
                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>아이디</label>
                    <input
                        type="text"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>비밀번호</label>
                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>
                {error && <p style={{ color: "red" }}>{error}</p>}
                <button type="submit">회원가입</button>
            </form>
        </div>
    );
}

export default Join;