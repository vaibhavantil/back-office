import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Button, Dropdown, Input } from 'semantic-ui-react';
import { memberStatus } from 'app/lib/selectOptions';

const MembersFilterContainer = styled.div`
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

export default class MembersFilter extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            searchValue: '',
            filter: ''
        };
    }

    keyPressHandler = e => {
        if (e.key === 'Enter') this.searchRequest();
    };

    inputChangeHandler = (e, { value }) => {
        this.setState({ searchValue: value });
    };

    searchRequest = () => {
        const { searchValue, filter } = this.state;
        this.props.searchMemberRequest({ query: searchValue, status: filter });
    };

    filterChangeHandler = (e, { value }) => {
        const searchValue = this.state.searchValue;
        this.props.setFilter({ query: searchValue, status: value });
        this.setState({ filter: value });
    };

    resetSearch = () => {
        const { setFilter } = this.props;
        setFilter({ status: 'ALL' });
        this.setState({ searchValue: '' });
    };

    componentDidMount() {
        this.setState({ filter: this.props.members.filter });
    }

    render() {
        const { members } = this.props;
        return (
            <React.Fragment>
                <MembersFilterContainer>
                    <Input
                        loading={members.requesting}
                        placeholder="Search..."
                        iconPosition="left"
                        onChange={this.inputChangeHandler}
                        onKeyPress={this.keyPressHandler}
                        action={{ icon: 'search', onClick: this.searchRequest }}
                        value={this.state.searchValue}
                    />

                    <label>Status: </label>
                    <Dropdown
                        onChange={this.filterChangeHandler}
                        options={memberStatus}
                        selection
                        value={members.filter}
                    />
                </MembersFilterContainer>
                <ResetButton onClick={this.resetSearch}>reset</ResetButton>
            </React.Fragment>
        );
    }
}

MembersFilter.propTypes = {
    members: PropTypes.object.isRequired,
    searchMemberRequest: PropTypes.func.isRequired,
    setFilter: PropTypes.func,
    sortMembersList: PropTypes.func.isRequired
};
