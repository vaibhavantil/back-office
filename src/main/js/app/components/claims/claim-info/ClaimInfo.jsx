import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Dropdown } from 'semantic-ui-react';
import { claimStatus } from 'app/lib/selectOptions';

const ClaimInfoContainer = styled.div`
    display: flex;
    justify-content: space-around;
    margin: 100px;
    padding: 30px;
    border: solid 1px #ccc;
`;

const Column = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
`;

export default class ClaimInfo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            status: 'OPEN',
            type: ''
        };
    }

    statusChangeHandler = (e, { value }) => {
        const { claimUpdate, id } = this.props;
        this.setState({ status: value });
        claimUpdate(id, { id, status: value }, 'status');
    };

    typeChangeHandler = (e, { value }) => {
        const { claimUpdate, id, types } = this.props;
        this.setState({ type: value });
        const type = types.filter(item => item.name === value)[0];
        claimUpdate(id, { id, ...type }, 'type');
    };

    componentDidMount() {
        const { userRequest, claimDetails: { data } } = this.props;
        if (data.userId) userRequest(data.userId);
        this.setState({
            status: data.status,
            type: data.type.name
        });
    }

    render() {
        const { user, types, claimDetails: { data } } = this.props;
        const { type, status } = this.state;
        return (
            <ClaimInfoContainer>
                <Column>
                    <span>Registration date: {data.registrationDate}</span>
                    <span>User: {user && user.firstName}</span>
                    <span>Audio:</span>
                    <audio src={data.url} controls />
                </Column>

                <Column>
                    Status
                    <Dropdown
                        onChange={this.statusChangeHandler}
                        options={claimStatus}
                        placeholder="Status"
                        selection
                        value={status}
                    />
                    Type
                    <Dropdown
                        onChange={this.typeChangeHandler}
                        options={types}
                        placeholder="Type"
                        selection
                        value={type}
                    />
                </Column>
            </ClaimInfoContainer>
        );
    }
}

ClaimInfo.propTypes = {
    user: PropTypes.object,
    claimUpdate: PropTypes.func.isRequired,
    id: PropTypes.string.isRequired,
    types: PropTypes.array.isRequired,
    userRequest: PropTypes.func.isRequired,
    claimDetails: PropTypes.object.isRequired
};
