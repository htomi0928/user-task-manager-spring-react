import React, { useState, useEffect } from 'react';
import axios from 'axios';

const UserTable = ({ onUserSelect }) => {
    const [users, setUsers] = useState([]);
    const [editingUserId, setEditingUserId] = useState(null);
    const [editFormData, setEditFormData] = useState({});

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const response = await axios.get('http://localhost:8081/api/users/all');
            setUsers(response.data);
        } catch (error) {
            console.error('Error fetching users', error);
        }
    };

    const deleteUser = async (userId) => {
        try {
            await axios.delete(`http://localhost:8081/api/users/${userId}`);
            fetchUsers(); // Refresh the list after deletion
        } catch (error) {
            console.error('Error deleting user', error);
        }
    };

    const handleEditClick = (user) => {
        setEditingUserId(user.id);
        setEditFormData({
            name: user.name,
            email: user.email,
            address: {
                country: user.address.country,
                postCode: user.address.postCode,
                city: user.address.city,
                street: user.address.street,
                houseNumber: user.address.houseNumber
            },
            password: user.password
        });
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        if (name.includes('address.')) {
            const addressField = name.split('.')[1];
            setEditFormData(prevState => ({
                ...prevState,
                address: {
                    ...prevState.address,
                    [addressField]: value
                }
            }));
        } else {
            setEditFormData({ ...editFormData, [name]: value });
        }
    };

    const handleSaveClick = async (userId) => {
        try {
            await axios.put(`http://localhost:8081/api/users/${userId}`, editFormData);
            setEditingUserId(null);
            fetchUsers(); // Refresh the list after saving
        } catch (error) {
            console.error('Error saving user', error);
        }
    };

    const handleCancelClick = () => {
        setEditingUserId(null);
    };

    return (
        <div>
            <h2>Users</h2>
            <table className="table">
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Country</th>
                        <th>Post code</th>
                        <th>City</th>
                        <th>Street name</th>
                        <th>Street number</th>
                        <th>Password</th>
                        <th>Deleted</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map(user => (
                        <tr key={user.id}>
                            {editingUserId === user.id ? (
                                <>
                                    <td>{user.id}</td>
                                    <td><input type="text" name="name" value={editFormData.name} onChange={handleInputChange} /></td>
                                    <td><input type="email" name="email" value={editFormData.email} onChange={handleInputChange} /></td>
                                    <td><input type="text" name="address.country" value={editFormData.address.country} onChange={handleInputChange} /></td>
                                    <td><input type="text" name="address.postCode" value={editFormData.address.postCode} onChange={handleInputChange} /></td>
                                    <td><input type="text" name="address.city" value={editFormData.address.city} onChange={handleInputChange} /></td>
                                    <td><input type="text" name="address.street" value={editFormData.address.street} onChange={handleInputChange} /></td>
                                    <td><input type="text" name="address.houseNumber" value={editFormData.address.houseNumber} onChange={handleInputChange} /></td>
                                    <td><input type="password" name="password" value={editFormData.password} onChange={handleInputChange} /></td>
                                    <td>{user.deleted.toString()}</td>
                                    <td>
                                        <button className="btn btn-success" onClick={() => handleSaveClick(user.id)}>Save</button>
                                        <button className="btn btn-secondary" onClick={handleCancelClick}>Cancel</button>
                                    </td>
                                </>
                            ) : (
                                <>
                                    <td>{user.id}</td>
                                    <td>{user.name}</td>
                                    <td>{user.email}</td>
                                    <td>{user.address.country}</td>
                                    <td>{user.address.postCode}</td>
                                    <td>{user.address.city}</td>
                                    <td>{user.address.street}</td>
                                    <td>{user.address.houseNumber}</td>
                                    <td>{user.password}</td>
                                    <td>{user.deleted.toString()}</td>
                                    <td>
                                        <button className="btn btn-primary" onClick={() => onUserSelect(user)}>View Tasks</button>
                                        <button className="btn btn-secondary" onClick={() => handleEditClick(user)}>Edit</button>
                                        <button className="btn btn-danger" onClick={() => deleteUser(user.id)} disabled={user.deleted}>Delete</button>
                                    </td>
                                </>
                            )}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default UserTable;
