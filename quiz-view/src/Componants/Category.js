import React, { useState, useEffect } from "react";

function Category(){
    const [categories, setCategories] = useState([]);
    const [newCategory, setNewCategory] = useState("");

    useEffect(() => {
        fetch("/api/categories")
            .then((response) => response.json())
            .then((data) => {
                if (Array.isArray(data)) {
                    setCategories(data);
                } else {
                    console.error("Unexpected data format:", data);
                    setCategories([]);
                }
            })
            .catch((error) => {
                console.error("Error fetching categories:", error);
                setCategories([]);
            });
    }, []);

    const handleCreateCategory = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch("/api/categories", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ name: newCategory }),
            });

            if (response.ok) {
                const createdCategory = await response.json();
                setCategories([...categories, createdCategory]);
                setNewCategory(""); // 입력 필드 초기화
            } else {
                console.error("Failed to create category");
            }
        } catch (error) {
            console.error("Error creating category:", error);
        }
    };
    return (
        <div>
            <h1>Category List</h1>
            <ul>
                {categories.map((category) => (
                    <li key={category.id}>{category.name}</li>
                ))}
            </ul>

            <form onSubmit={handleCreateCategory}>
                <input
                    type="text"
                    value={newCategory}
                    onChange={(e) => setNewCategory(e.target.value)}
                    placeholder="New Category"
                    required
                />
                <button type="submit">Create Category</button>
            </form>
        </div>
    );
}

export default Category;