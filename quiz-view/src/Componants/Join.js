import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../Css/formStyle.css"; // CSS 파일 임포트

function Join() {
    const [formData, setFormData] = useState({
        name: "",
        username: "",
        password: "",
    });
    const navigate = useNavigate();
    const [error, setError] = useState(null);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleJoin = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('/api/members/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
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
        <div className="form-container">
            <div className="form-box">
                <h2>회원가입</h2>
                <form className="join-form" onSubmit={handleJoin}>
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
                    {error && <p className="error-message">{error}</p>}
                    <button type="submit">회원가입</button>
                </form>
            </div>
        </div>
    );
}

export default Join;
