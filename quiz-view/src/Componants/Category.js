import React, { useState } from "react";
import "../Css/Common.css"; // CSS 파일 경로에 맞게 수정

function Category(){
    //const [newCategory, setNewCategory] = useState("");

    const [formData, setFormData] = useState({
        name: "",
    });

    // Handle input change
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    // 카테고리 생성 함수
    const handleCreateCategory = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch("/api/category/create", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(formData),
            });
            console.log(formData); // formData가 제대로 전달되는지 확인

            console.log(response);
            if (response.ok) {
                setFormData({name: ""}); // 입력 필드 초기화
                alert("카테고리 등록 성공");
            } else {
                console.error("Failed to create category");
            }
        } catch (error) {
            console.error("Error creating category:", error);
        }
    };

    return (
        <div className="wrapper cateRegist">
            <h1>카테고리 등록</h1>

            <form onSubmit={handleCreateCategory}>
                <input
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    placeholder="New Category"
                    required
                />
                <button type="submit">등록</button>
            </form>
        </div>
    );
}

export default Category;
