import React, { useState } from "react";
import "../Css/Quiz.css";
import QuizQuestion from "./QuizQuestion";
import QuizOptions from "./QuizOptions";

const Quiz = () => {
    const [selectedOption, setSelectedOption] = useState(null);

    //임시 데이터
    const question =
    "중국어로 ‘여행객’의 의미를 가진 단어이며, 우리나라로 오는 중국인 관광객을 말하는 단어로 맞는 것은?";
    const options = ["유커", "왕퉁", "장첸"];

    const handleSelectOption = (option) => {
        setSelectedOption(option);
    };

    return (
        <div className="wrapper">
            <div className="status-bar">
                <div className="status-item">남은 개수: 9개</div>
                <div className="status-item">맞힌 개수: 1개</div>
                <div className="status-item">현재 순위: 3등</div>
            </div>
            <div className="timer-bar">
                <div className="timer" style={{ width: '50%' }}></div>
            </div>
            <div className="quiz-container">
                <QuizQuestion question={question} />
                <QuizOptions
                    options={options}
                    selectedOption={selectedOption}
                    onSelectOption={handleSelectOption}
                />
            </div>
        </div>
    );
};

export default Quiz;


