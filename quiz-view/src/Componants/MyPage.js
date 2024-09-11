import React, { useEffect, useState } from "react";
import api from "./api";
import "../Css/Common.css";
function MyPage() {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        // 유저 정보를 가져오는 함수
        const fetchUser = async () => {
            try {
                const response = await api.get('/api/members/getUser'); // API URL 수정
                setUser(response.data);
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        fetchUser();
    }, []);

    const handleUpdate = () => {
        // 회원 수정 로직
        // 예를 들어, 수정할 페이지로 이동하거나 모달을 열 수 있습니다.
        alert("회원 수정 페이지로 이동합니다."); // 실제 로직으로 변경 필요
    };

    const handleDeleteAccount = async () => {
        const confirmDelete = window.confirm("정말로 회원 탈퇴하시겠습니까?");
        if (confirmDelete) {
            try {
                await api.delete(`/api/members/deleteAccount/${user.id}`); // API에 맞게 수정
                alert("회원 탈퇴가 완료되었습니다.");
                // 로그아웃 처리 후 홈으로 이동하거나 다른 페이지로 리다이렉트
            } catch (err) {
                setError(err);
            }
        }
    };

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error.message}</div>;
    }

    return (
        <div className="wrapper mypage">
            {user ? (
                <div>
                    <h1>마이페이지</h1>
                    <p><strong>이름:</strong> {user.name}</p>
                    <p><strong>아이디:</strong> {user.username}</p>
                    <p className="pwd"><strong>비밀번호:</strong> <input type="password" /></p>
                    <div className="button-group">
                        <button onClick={handleUpdate}>회원 수정</button>
                        <button className="delete-button" onClick={handleDeleteAccount}>회원 탈퇴</button>
                    </div>
                </div>
            ) : (
                <div>유저 정보를 찾을 수 없습니다.</div>
            )}
        </div>
    );
}

export default MyPage;