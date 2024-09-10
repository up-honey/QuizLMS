import React, { useState, useEffect } from 'react';
import api, { fetchCategories } from '../api'; // api 모듈을 사용
import { useNavigate } from 'react-router-dom';

const QuizCreate = () => {
    const navigate = useNavigate();
    const [categories, setCategories] = useState([]);
    const [form, setForm] = useState({
        title: '',
        categoryName: '',
        answer: '',
        options: ['', '', '', ''] // 옵션을 4개로 초기화
    });

    //카테고리 리스트 호출
    useEffect(() => {
        const loadCategories = async () => {
            setCategories(await fetchCategories()); // await로 호출하여 결과를 바로 설정
        };
        loadCategories();
    }, []);

    const handleChange = (e) => {
        const { name, value, dataset } = e.target;
        if (name === 'options') {
            const index = parseInt(dataset.index, 10);
            setForm(prevForm => {
                const newOptions = [...prevForm.options];
                newOptions[index] = value;
                return { ...prevForm, options: newOptions };
            });
        } else {
            setForm(prevForm => ({ ...prevForm, [name]: value }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post('/api/quiz/create', form);
            alert('Quiz created successfully');
            navigate('/quiz/list');
        } catch (error) {
            console.error(error);
            alert('Failed to create quiz');
        }
    };

    return (
        <div className="container quiz">
            <div className='wrapper'>
            <h1>퀴즈 생성</h1>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>카테고리: </label>
                        <select
                            id="categoryName"
                            name="categoryName"
                            value={form.categoryName}
                            onChange={handleChange}
                            className="form-control"
                            required
                        >
                            <option value="">카테고리를 선택하세요</option>
                            {categories.map(category => (
                                <option key={category.id} value={category.name}>
                                    {category.name}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div>
                        <label>문제: </label>
                        <input
                            type="text"
                            name="title"
                            value={form.title}
                            onChange={handleChange}
                            className="form-control"
                            required
                        />
                    </div>
                    <div>
                        <label>정답: </label>
                        <input
                            type="text"
                            name="answer"
                            value={form.answer}
                            onChange={handleChange}
                            className="form-control"
                            required
                        />
                    </div>
                    <div>
                        <label>정답/오답: </label>
                        {form.options.map((option, index) => (
                            <div key={index}>
                                <input
                                    type="text"
                                    name="options"
                                    data-index={index}
                                    value={option}
                                    onChange={handleChange}
                                    className="form-control"
                                    required
                                />
                            </div>
                        ))}
                    </div>
                    <button type="submit" className="btn btn-primary">생성</button>
                </form>
            </div>
        </div>
    );
};

export default QuizCreate;
