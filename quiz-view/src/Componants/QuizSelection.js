import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { fetchCategories } from "./api";

function QuizSelection() {
    const [categories, setCategories] = useState([]);

    useEffect(() => {
        const loadCategories = async () => {
            setCategories(await fetchCategories());
        };
        loadCategories();
    }, []);

    return(
        <div className="quiz-selection">
            <div className="wrapper">
                <h1>언제나 재미있는 퀴즈!</h1>
                <p>오늘도 재미있게 퀴즈 풀어봐요~</p>
                <div className="quiz-category">
                    {categories.length > 0 ? (
                        categories.map((category, index) => (
                        <Link
                            key={category.id}
                            to={`/quiz/category/${category.name}`}
                            className={`category-item ${index % 2 === 0 ? 'blue' : 'yellow'}`} // 인덱스에 따라 색상 결정
                        >
                            <div className="icon">{index % 2 === 0 ? "💡" : "🤔"}</div>
                            <span>{category.name}</span>
                        </Link>
                        ))
                    ) : (
                        <div>카테고리가 없습니다.</div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default QuizSelection;