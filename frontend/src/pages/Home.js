// frontend/src/pages/Home.js
import React, { useState, useEffect } from 'react';
import {
    ApiClient,
    BanksApi,
    UserBanksApi,
    UserBankCostsApi,
    UserSpendingApi,
    AddUserBank as BankModel,
    UpdateSpending
} from 'ccs-openapi-client';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate } from 'react-router-dom';
import '../index.css';
import {logout, getAuthToken} from '../auth';

const PAGE_SIZE = 25;
const API_BASE = process.env.REACT_APP_API_BASE_URL;
ApiClient.instance.basePath = API_BASE;

export default function Home() {
    const [supportedBanks, setSupportedBanks]         = useState([]);
    const [selectedBankId, setSelectedBankId]         = useState('');
    const [userBanks, setUserBanks]                   = useState([]);
    const [selectedUserBankId, setSelectedUserBankId] = useState('');
    const [errorMessage, setErrorMessage]             = useState(null);

    // Transactions state
    const [costs, setCosts]         = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const [page, setPage]           = useState(1);

    // Spending state
    const [limit, setLimit]         = useState(null);
    const [current, setCurrent]     = useState(null);
    const [newLimit, setNewLimit]   = useState('');
    const navigate = useNavigate();

    // Auto-hide errors
    useEffect(() => {
        if (!errorMessage) return;
        const timer = setTimeout(() => setErrorMessage(null), 3000);
        return () => clearTimeout(timer);
    }, [errorMessage]);

    // Initial load
    useEffect(() => {
        const token = getAuthToken();
        if (token) {
            ApiClient.instance.defaultHeaders = {
                ...ApiClient.instance.defaultHeaders,
                Authorization: token,
            };
        }
        loadSupportedBanks();
        loadUserBanks();
    }, []);

    // Reload costs, count, and spending when bank or page changes
    useEffect(() => {
        if (!selectedUserBankId) {
            setCosts([]); setTotalCount(0); setPage(1);
            setLimit(null); setCurrent(null); setNewLimit('');
            return;
        }
        const bankId = parseInt(selectedUserBankId, 10);

        // count & costs
        new UserBankCostsApi().getUserBankCostsCount(bankId, (err, data) => {
            if (!err) setTotalCount(data.count);
        });
        new UserBankCostsApi().getUserBankCosts(bankId, page, (err, data) => {
            if (!err) setCosts(Array.isArray(data.values) ? data.values : []);
        });

        // spending (limit & current)
        new UserSpendingApi().getUserSpending(bankId, (err, data) => {
            if (!err) {
                setLimit(data.maxAmount != null ? data.maxAmount : null);
                setCurrent(data.currentAmount != null ? data.currentAmount : null);
                setNewLimit(''); // сброс инпута
            }
        });
    }, [selectedUserBankId, page]);

    function loadSupportedBanks() {
        new BanksApi().getSupportedBanks((err, data) => {
            if (!err) {
                const arr = Array.isArray(data.values) ? data.values : [];
                setSupportedBanks(arr);
                if (arr.length) setSelectedBankId(arr[0].id.toString());
            }
        });
    }

    function loadUserBanks() {
        new UserBanksApi().getUserBanks((err, data) => {
            if (!err) {
                const arr = Array.isArray(data.values) ? data.values : [];
                setUserBanks(arr);
                setSelectedUserBankId(arr.length ? arr[0].id.toString() : '');
            }
        });
    }

    const handleAddBank = () => {
        setErrorMessage(null);
        if (!selectedBankId) return;
        const bankToAdd = supportedBanks.find(b => b.id.toString() === selectedBankId);
        if (!bankToAdd) return;
        new UserBanksApi().addUserBank(
            { bank: new BankModel(bankToAdd.id, bankToAdd.description) },
            (err, _d, resp) => {
                if (err && resp?.status === 400) {
                    setErrorMessage(resp.body?.message || 'Этот банк уже добавлен.');
                } else {
                    loadUserBanks();
                }
            }
        );
    };

    const handleSetLimit = () => {
        setErrorMessage(null);
        if (!selectedUserBankId || newLimit.trim() === '') return;
        const bankId = parseInt(selectedUserBankId, 10);
        const maxAmt = ApiClient.convertToType(newLimit, 'Number');
        const update = new UpdateSpending(maxAmt);
        new UserSpendingApi().updateUserSpending(
            bankId,
            { updateSpending: update },
            (err, _d, resp) => {
                if (err) {
                    if (resp?.status === 400) {
                        setErrorMessage(resp.body?.message);
                    }
                } else {
                    // refresh
                    new UserSpendingApi().getUserSpending(bankId, (e2, data2) => {
                        if (!e2) {
                            setLimit(data2.maxAmount ?? null);
                            setCurrent(data2.currentAmount ?? null);
                            setNewLimit('');
                        }
                    });
                }
            }
        );
    };

    const totalPages = Math.ceil(totalCount / PAGE_SIZE);

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <div className="container py-4 position-relative">
            {/* Header */}
            <header className="mb-4 text-center position-relative">
                <h1 className="display-4 text-primary">CoinKeeper</h1>
                <button
                    className="btn btn-outline-danger position-absolute"
                    style={{ top: 0, right: 0, margin: '1rem' }}
                    onClick={handleLogout}
                >
                    Log out
                </button>
            </header>

            {/* Banks Section */}
            <div className="row mb-4">
                {/* Available Banks */}
                <div className="col-md-6 mb-3 position-relative">
                    {errorMessage && (
                        <div
                            className="alert alert-danger position-absolute"
                            style={{ top: '0.75rem', left: '0.75rem', zIndex: 10 }}
                        >
                            {errorMessage}
                        </div>
                    )}
                    <div className="card shadow-sm">
                        <div className="card-body">
                            <h2 className="card-title h5">Доступные банки</h2>
                            <div className="input-group mt-3">
                                <select
                                    className="form-select"
                                    value={selectedBankId}
                                    onChange={e => setSelectedBankId(e.target.value)}
                                >
                                    {supportedBanks.map(bank => (
                                        <option key={bank.id} value={bank.id}>
                                            {bank.description}
                                        </option>
                                    ))}
                                </select>
                                <button className="btn btn-primary" onClick={handleAddBank}>
                                    Добавить
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Selected Banks */}
                <div className="col-md-6 mb-3">
                    <div className="card shadow-sm">
                        <div className="card-body">
                            <h2 className="card-title h5">Выбранные банки</h2>
                            {userBanks.length > 0 ? (
                                <div className="input-group mt-3">
                                    <select
                                        className="form-select"
                                        value={selectedUserBankId}
                                        onChange={e => {
                                            setSelectedUserBankId(e.target.value);
                                            setPage(1);
                                        }}
                                    >
                                        {userBanks.map(bank => (
                                            <option key={bank.id} value={bank.id}>
                                                {bank.description}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                            ) : (
                                <p className="text-muted mt-3">Пока нет добавленных банков.</p>
                            )}
                        </div>
                    </div>
                </div>
            </div>

            {/* Limit and Overview Section */}
            <div className="row mb-4">
                {/* Set Limit */}
                <div className="col-md-6 mb-3">
                    <div className="card shadow-sm">
                        <div className="card-body">
                            <h2 className="card-title h5">Лимит на месяц</h2>
                            {limit != null ? (
                                <p className="display-6">{limit}</p>
                            ) : (
                                <p className="text-muted">Лимит не установлен</p>
                            )}
                            <div className="input-group mt-3">
                                <input
                                    type="number"
                                    className="form-control"
                                    placeholder="Введите новый лимит"
                                    value={newLimit}
                                    onChange={e => setNewLimit(e.target.value)}
                                />
                                <button className="btn btn-primary" onClick={handleSetLimit}>
                                    Установить
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                {/* Current Sum */}
                <div className="col-md-6 mb-3">
                    <div className="card shadow-sm">
                        <div className="card-body">
                            <h2 className="card-title h5">Текущая сумма</h2>
                            {current != null ? (
                                <p
                                    className="display-6"
                                    style={{ color: limit != null && current > limit ? 'red' : 'green' }}
                                >
                                    {current}
                                </p>
                            ) : (
                                <p className="text-muted">Нет данных</p>
                            )}
                        </div>
                    </div>
                </div>
            </div>

            {/* Transactions Section */}
            <div className="card shadow-sm mb-4">
                <div className="card-body">
                    <div className="d-flex justify-content-between align-items-center mb-3">
                        <h2 className="card-title h5 mb-0">Транзакции</h2>
                        <button
                            className="btn btn-success"
                            onClick={() => (window.location.href = '/analytics')}
                        >
                            Аналитика
                        </button>
                    </div>
                    <div className="table-responsive">
                        <table className="table table-striped mb-3">
                            <thead>
                            <tr>
                                <th>Amount</th>
                                <th>Description</th>
                                <th>Date</th>
                                <th>Category description</th>
                            </tr>
                            </thead>
                            <tbody>
                            {costs.length > 0 ? (
                                costs.map(cost => (
                                    <tr key={cost.id}>
                                        <td>{cost.amount}</td>
                                        <td>{cost.description}</td>
                                        <td>{new Date(cost.createdAt).toLocaleDateString()}</td>
                                        <td>{cost.categoryDescription}</td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="4" className="text-center text-muted">
                                        Нет транзакций
                                    </td>
                                </tr>
                            )}
                            </tbody>
                        </table>
                    </div>
                    {totalPages > 1 && (
                        <nav>
                            <ul className="pagination justify-content-end mb-0">
                                <li className={`page-item ${page === 1 ? 'disabled' : ''}`}>
                                    <button className="page-link" onClick={() => setPage(page - 1)}>
                                        « Пред
                                    </button>
                                </li>
                                <li className="page-item disabled">
                  <span className="page-link">
                    {page} / {totalPages}
                  </span>
                                </li>
                                <li className={`page-item ${page === totalPages ? 'disabled' : ''}`}>
                                    <button className="page-link" onClick={() => setPage(page + 1)}>
                                        След »
                                    </button>
                                </li>
                            </ul>
                        </nav>
                    )}
                </div>
            </div>

            {/* Footer */}
            <footer className="text-center text-muted small">
                © {new Date().getFullYear()} CoinKeeper
            </footer>
        </div>
    );
}
