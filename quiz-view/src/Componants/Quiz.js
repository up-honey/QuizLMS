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
        <div className="quiz-container">
            <QuizQuestion question={question} />
            <QuizOptions
                options={options}
                selectedOption={selectedOption}
                onSelectOption={handleSelectOption}
            />
        </div>
    );
};

export default Quiz;


