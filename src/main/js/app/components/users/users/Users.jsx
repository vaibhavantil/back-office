import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Button, Dropdown, Input, Header } from 'semantic-ui-react';
import UsersList from '../users-list/UsersList';
import BackLink from 'components/shared/link/BackLink';
import { userStatus } from 'app/lib/selectOptions';

const UsersFilter = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    width: 100%;
`;

const ResetButton = styled(Button)`
    &&& {
        margin-top: 10px;
    }
`;

export default class Users extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            searchValue: '',
            filter: ''
        };
    }

    inputChangeHandler = (e, { value }) => {
        this.setState({ searchValue: value });
    };

    keyPressHandler = e => {
        if (e.key === 'Enter') this.searchRequest();
    };

    searchRequest = () => {
        const { searchValue, filter } = this.state;
        this.props.searchUserRequest({ query: searchValue, status: filter });
    };

    filterChangeHandler = (e, { value }) => {
        const searchValue = this.state.searchValue;
        this.props.setFilter({ status: value, query: searchValue });
        if (value === 'ALL') this.resetSearch();
        this.setState({ filter: value });
    };

    resetSearch = () => {
        const { usersRequest, setFilter } = this.props;
        setFilter({ status: 'ALL' });
        this.setState({ searchValue: '' }, () => usersRequest());
    };

    componentDidMount() {
        const { users } = this.props;
        this.setState({ filter: users.filter });
        if (!users.list.length) {
            this.resetSearch();
        }
    }

    render() {
        const {
            users,
            messages,
            newMessagesReceived,
            setActiveConnection,
            client
        } = this.props;
        const { searchValue } = this.state;
        return (
            <React.Fragment>
                <Header size="huge">Members</Header>
                <BackLink />
                <UsersFilter>
                    <Input
                        loading={users.requesting}
                        placeholder="Search..."
                        iconPosition="left"
                        onChange={this.inputChangeHandler}
                        onKeyPress={this.keyPressHandler}
                        action={{ icon: 'search', onClick: this.searchRequest }}
                        value={searchValue}
                    />

                    <label>Status: </label>
                    <Dropdown
                        onChange={this.filterChangeHandler}
                        options={userStatus}
                        selection
                        value={users.filter}
                    />
                </UsersFilter>
                <ResetButton onClick={this.resetSearch}>reset</ResetButton>
                <UsersList
                    users={users.list}
                    messages={messages}
                    newMessagesReceived={newMessagesReceived}
                    setActiveConnection={setActiveConnection}
                    client={client}
                />
            </React.Fragment>
        );
    }
}

Users.propTypes = {
    users: PropTypes.object.isRequired,
    messages: PropTypes.object.isRequired,
    newMessagesReceived: PropTypes.func.isRequired,
    setActiveConnection: PropTypes.func.isRequired,
    client: PropTypes.object.isRequired,
    searchUserRequest: PropTypes.func.isRequired,
    usersRequest: PropTypes.func.isRequired,
    setFilter: PropTypes.func
};
