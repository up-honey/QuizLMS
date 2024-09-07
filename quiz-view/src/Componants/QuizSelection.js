import React from "react";
import { Link } from "react-router-dom";

function QuizSelection() {
    return(
        <div className="quiz-selection">
            <div className="wrapper">
                <h1>언제나 재미있는 퀴즈!</h1>
                <p>오늘도 재미있게 퀴즈 풀어봐요~</p>
                <div className="quiz-category">
                    <Link to="/knowledge-quiz" className="category-item blue">
                    <div className="icon">💡</div>
                    <span>역사 퀴즈</span>
                    </Link>
                    <Link to="/dad-joke-quiz" className="category-item blue">
                    <div className="icon">👨‍🦳</div>
                    <span>영어 퀴즈</span>
                    </Link>
                    <Link to="/imagination-quiz" className="category-item yellow">
                    <div className="icon">🤔</div>
                    <span>수학 퀴즈</span>
                    </Link>
                    <Link to="/balance-quiz" className="category-item yellow">
                    <div className="icon">⚡</div>
                    <span>밸런스 퀴즈</span>
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default QuizSelection;