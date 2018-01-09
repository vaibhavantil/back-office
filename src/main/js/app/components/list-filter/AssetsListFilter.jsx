import React from 'react';
import { Dropdown } from 'semantic-ui-react';
import { assetStates } from 'app/lib/selectOptions';

/* eslint-disable react/prop-types */
export default class Fliter extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            filters: []
        };
    }

    componentDidMount() {
        if (!assetStates[6]) {
            assetStates.push({
                key: 7,
                text: 'ALL',
                value: 'ALL'
            });
        }
        this.setState({
            filters: assetStates
        });
    }

    render() {
        return (
            <div className="filter">
                <label className="filter__label">Select asset state: </label>
                <Dropdown
                    onChange={this.props.filterChange}
                    options={this.state.filters}
                    selection
                    value={this.props.activeFilter}
                />
            </div>
        );
    }
}
