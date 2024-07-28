import React, { useState, useEffect } from 'react';
import axios from 'axios';

// Define component
const UserTable = ({ onUserSelect }) => {
    // List of users
    const [users, setUsers] = useState([]);
    // Currently edited user's id
    const [editingUserId, setEditingUserId] = useState(null);
    // Edit request data
    const [editFormData, setEditFormData] = useState({});

    // Function to fetch users when this component is called
    useEffect(() => {
        fetchUsers();
    }, []);

    // Function to fetch users from the API
    const fetchUsers = async () => {
        try {
            const response = await axios.get('http://localhost:8081/api/users/all');
            setUsers(response.data);
        } catch (error) {
            console.error('Error fetching users', error);
        }
    };

    // Function to delete a user by Id
    const deleteUser = async (userId) => {
        try {
            await axios.delete(`http://localhost:8081/api/users/${userId}`);
            fetchUsers();
        } catch (error) {
            console.error('Error deleting user', error);
        }
    };

    // Function to handle the edit button click
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
    // Function to handle input changes in the edit form
    const handleInputChange = (e) => {
        // Get name and value from the event  target
        const { name, value } = e.target;
        // Check if the input field is a part of the address object
        if (name.includes('address.')) {
            const addressField = name.split('.')[1];
            // Update the edit form data state for the specific address field
            setEditFormData(prevState => ({ 
                ...prevState,  // Keep the previous value unchanged
                address: {
                    ...prevState.address,
                    [addressField]: value // Update new address value
                }
            }));
        } else {
            // Update new value
            setEditFormData({ ...editFormData, [name]: value });
        }
    };

    // Function to update user with the API
    const handleSaveClick = async (userId) => {
        try {
            await axios.put(`http://localhost:8081/api/users/${userId}`, editFormData);
            setEditingUserId(null);
            fetchUsers();
        } catch (error) {
            console.error('Error saving user', error);
        }
    };

    // Function to close editing if canceleld
    const handleCancelClick = () => {
        setEditingUserId(null);
    };

    return (
        <div className='container-fluid'>
            <h2 className="my-4">Users</h2>
            <table className="table table-striped table-hover w-100">
                <thead className="thead-dark">
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
                        <th style={{ width: '200px' }}>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map(user => (
                        <tr key={user.id}>
                            {editingUserId === user.id ? (
                                <>
                                    <td>{user.id}</td>
                                    <td><input type="text" name="name" className="form-control" value={editFormData.name} onChange={handleInputChange} /></td>
                                    <td><input type="email" name="email" className="form-control" value={editFormData.email} onChange={handleInputChange} /></td>
                                    <td><input type="text" name="address.country" className="form-control" value={editFormData.address.country} onChange={handleInputChange} /></td>
                                    <td><input type="text" name="address.postCode" className="form-control" value={editFormData.address.postCode} onChange={handleInputChange} /></td>
                                    <td><input type="text" name="address.city" className="form-control" value={editFormData.address.city} onChange={handleInputChange} /></td>
                                    <td><input type="text" name="address.street" className="form-control" value={editFormData.address.street} onChange={handleInputChange} /></td>
                                    <td><input type="text" name="address.houseNumber" className="form-control" value={editFormData.address.houseNumber} onChange={handleInputChange} /></td>
                                    <td><input type="password" name="password" className="form-control" value={editFormData.password} onChange={handleInputChange} /></td>
                                    <td>{user.deleted.toString()}</td>
                                    <td>
                                        <button className="btn btn-success w-100" onClick={() => handleSaveClick(user.id)}>Save</button>
                                        <button className="btn btn-secondary w-100" onClick={handleCancelClick}>Cancel</button>
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
                                        <button className="btn btn-primary w-100" onClick={() => onUserSelect(user)}>View Tasks</button>
                                        <button className="btn btn-secondary w-100" onClick={() => handleEditClick(user)}>Edit</button>
                                        <button className="btn btn-danger w-100" onClick={() => deleteUser(user.id)} disabled={user.deleted}>Delete</button>
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
